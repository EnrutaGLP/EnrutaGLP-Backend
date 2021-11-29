package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.enrutaglp.backend.algorithm.AstarFunciones;
import com.enrutaglp.backend.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class Punto {

	@Setter
	private int id;
	private int ubicacionX;
	private int ubicacionY;
	@Setter
	private int orden; 
	@Setter
	private int idBloqueo;
	@Setter
	private String codigoPedido;
	@Setter
	private boolean planta;
	@Setter
	private double astarF;
	@Setter
	private double astarG;
	@Setter
	private double astarH;
	@Setter
	private Punto antecesor;
	
	public Punto() {
		codigoPedido = null;
	}
	
	public Punto(int ubicacionX, int ubicacionY) {
		this.ubicacionX = ubicacionX; 
		this.ubicacionY = ubicacionY;
	}
	
	public Punto(int id, int ubicacionX, int ubicacionY, int orden, int idBloqueo) {
		this.id = id;
		this.ubicacionX = ubicacionX;
		this.ubicacionY = ubicacionY;
		this.orden = orden;
		this.idBloqueo = idBloqueo;
	}
	
	
	
	public Punto clone() {
		Punto p = new Punto(); 
		p.ubicacionX = this.ubicacionX; 
		p.ubicacionY = this.ubicacionY; 
		p.orden = this.orden; 
		p.planta = this.planta; 
		p.codigoPedido = this.codigoPedido;
		return p; 
	}
	
	public Punto(int ubicacionX, int ubicacionY, int orden) {
		this.ubicacionX = ubicacionX;
		this.ubicacionY = ubicacionY;
		this.orden = orden;
		
		if((this.ubicacionX==12)&(this.ubicacionY==8)) {
			this.setPlanta(true);
		}
		else {
			this.setPlanta(false);
		}
		this.codigoPedido = null;
	}
	
	public Punto(int ubicacionX, int ubicacionY, int orden, String codigoPedido) {
		this.ubicacionX = ubicacionX;
		this.ubicacionY = ubicacionY;
		this.orden = orden;
		
		if((this.ubicacionX==12)&(this.ubicacionY==8)) {
			this.setPlanta(true);
		}
		else {
			this.setPlanta(false);
			this.codigoPedido = codigoPedido; 
		}
	}
	
	public void setUbicacionX(int ubicacionX) {
		this.ubicacionX = ubicacionX;
		
		if((this.ubicacionX==12)&(this.ubicacionY==8)) {
			this.setPlanta(true);
		}
		else {
			this.setPlanta(false);
		}
		
	}
	
	public void setUbicacionY(int ubicacionY) {
		this.ubicacionY = ubicacionY;
		
		if((this.ubicacionX==12)&(this.ubicacionY==8)) {
			this.setPlanta(true);
		}
		else {
			this.setPlanta(false);
		}
		
	}


	public double calcularDistanciasNodos(Punto puntoNodo) {
		  double x1 = this.getUbicacionX(); 
		  double y1 = this.getUbicacionY(); 
		  double x2 = puntoNodo.getUbicacionX(); 
		  double y2 = puntoNodo.getUbicacionY();
		  
		  return Math.abs(y2 - y1)+ Math.abs(x2 - x1);
	}
	

	
	
	
	public List<Punto> pruebaAStar(Punto puntoFinal, List<Bloqueo> bloqueos) {
		/*
		 * Punto intermedio: punto esquina
		 * 
		 */
		AstarFunciones af = new AstarFunciones();
		List<Punto> puntosIntemedios = new ArrayList<Punto>();

		
		/*
		this.distanciaRecorrida = this.puntos.get(this.puntos.size()-2).calcularDistanciasNodos(this.puntos.get(this.puntos.size()-1));
		int tiempo = (int) (this.distanciaRecorrida/this.camion.getTipo().getVelocidadPromedio());
		fechafinal = fechaIni.plusHours(tiempo);
		*/
		//verificar fechaini y fechafin, si no hay bloqueos devolver listapuntos
		
		//si hay bloqueos A *
		Punto pEsquina1 = new Punto(this.getUbicacionX(), puntoFinal.getUbicacionY(), this.getOrden());
		Punto pEsquina2 = new Punto(puntoFinal.getUbicacionX(), this.getUbicacionY(), this.getOrden());
		
		if (!af.hayBloqueosEntre (this, pEsquina1, bloqueos) &&
				!af.hayBloqueosEntre (pEsquina1, puntoFinal, bloqueos)) {
//			if (!af.mismaPosicion(puntoFinal, pEsquina1) && !af.mismaPosicion(this, pEsquina1)) {
//				puntosIntemedios.add(pEsquina1);
//			}
			puntosIntemedios.add(pEsquina1);
			return puntosIntemedios;
		}
		
		if (!af.hayBloqueosEntre (this, pEsquina2, bloqueos) &&
				!af.hayBloqueosEntre (pEsquina2, puntoFinal, bloqueos)) {
//			if (!af.mismaPosicion(puntoFinal, pEsquina2) && !af.mismaPosicion(this, pEsquina2)) {
//				puntosIntemedios.add(pEsquina2);
//			}
			puntosIntemedios.add(pEsquina2);
			return puntosIntemedios;
		}
		

		return af.astarAlgoritmo(this, puntoFinal, bloqueos);

	}
	
	public List<Punto> getPuntosIntermedios(Punto puntoFinal, LocalDateTime fechaIni, Camion camion, List<Bloqueo> bloqueos){
		List<Punto> puntosIntemedios = new ArrayList<Punto>();
		
		puntosIntemedios.add(this);
		puntosIntemedios.add(puntoFinal);
		Punto nuevoPunto1 = new Punto(this.ubicacionX, puntoFinal.getUbicacionY(), this.orden + 1, this.codigoPedido);
		puntosIntemedios.add(1,nuevoPunto1);
		double distanciaPuntosActual1 = Utils.calcularDistanciaTodosPuntos(puntosIntemedios);
		long tiempo1 = (long) (distanciaPuntosActual1/camion.getTipo().getVelocidadPromedio() * 3600);
		LocalDateTime fechaHoraEntrega1 = fechaIni.plusSeconds(tiempo1);
		
		List<Bloqueo> bloqueosActuales1 = new ArrayList<Bloqueo>();
		
		for (int i = 0; i < bloqueos.size(); i++) {
			if(bloqueos.get(i).getFechaInicio().isBefore(fechaHoraEntrega1) && 
					fechaIni.isBefore(bloqueos.get(i).getFechaFin())) {
				bloqueosActuales1.add(bloqueos.get(i));
			}
		}
		
		if(bloqueosActuales1.size()==0) {
			puntosIntemedios.remove(0);
			puntosIntemedios.remove(puntosIntemedios.size()-1);
			return puntosIntemedios;
		}
		
		puntosIntemedios.clear();
		puntosIntemedios.add(this);
		puntosIntemedios.add(puntoFinal);
		Punto nuevoPunto2 = new Punto(puntoFinal.getUbicacionX(), this.ubicacionY, this.orden + 1, this.codigoPedido);
		puntosIntemedios.add(1,nuevoPunto2);
		double distanciaPuntosActual2 = Utils.calcularDistanciaTodosPuntos(puntosIntemedios);
		long tiempo2 = (long) (distanciaPuntosActual2/camion.getTipo().getVelocidadPromedio() * 3600);
		LocalDateTime fechaHoraEntrega2 = fechaIni.plusSeconds(tiempo2);
		
		List<Bloqueo> bloqueosActuales2 = new ArrayList<Bloqueo>();
		
		for (int i = 0; i < bloqueos.size(); i++) {
			if(bloqueos.get(i).getFechaInicio().isBefore(fechaHoraEntrega2) && 
					fechaIni.isBefore(bloqueos.get(i).getFechaFin())) {
				bloqueosActuales2.add(bloqueos.get(i));
			}
		}
		
		if(bloqueosActuales2.size()==0) {
			puntosIntemedios.remove(0);
			puntosIntemedios.remove(puntosIntemedios.size()-1);
			return puntosIntemedios;
		}
		
		
		puntosIntemedios.clear();
		
		List<Bloqueo> bloqueosActuales = bloqueosActuales1.size()<bloqueosActuales2.size() ?  bloqueosActuales1 : bloqueosActuales2;
		double distanciaPuntosActual = 0;
		long tiempo = 0;
		LocalDateTime fechaHoraEntrega = null;
		
		int numBloqueos = 0;
		
		while(bloqueosActuales.size()>numBloqueos) {
			//AStar aStar = new AStar(this, puntoFinal, camion, bloqueosActuales);
			AstarFunciones af = new AstarFunciones();
			puntosIntemedios.clear();
			puntosIntemedios = af.astarAlgoritmo(this, puntoFinal, bloqueosActuales);
			puntosIntemedios.add(0, this);
			puntosIntemedios.add(puntoFinal);
			
			distanciaPuntosActual = Utils.calcularDistanciaTodosPuntos(puntosIntemedios);
			tiempo = (long) (distanciaPuntosActual/camion.getTipo().getVelocidadPromedio() * 3600);
			fechaHoraEntrega = fechaIni.plusSeconds(tiempo);
			
			numBloqueos = bloqueosActuales.size();
			
			bloqueosActuales.clear();
			for (int i = 0; i < bloqueos.size(); i++) {
				if(bloqueos.get(i).getFechaInicio().isBefore(fechaHoraEntrega) && 
						fechaIni.isBefore(bloqueos.get(i).getFechaFin())) {
					bloqueosActuales.add(bloqueos.get(i));
				}
			}
		}
		
		puntosIntemedios.remove(0);
		puntosIntemedios.remove(puntosIntemedios.size()-1);
		return puntosIntemedios;
	}
	
	
	public List<Punto> get_intermediate_points (Punto final_point, LocalDateTime ini_date, List<Bloqueo> locks){
		
		
		Punto corner1 = new Punto(this.getUbicacionX(), final_point.getUbicacionY(), this.getOrden() + 1, this.getCodigoPedido());
		Punto corner2 = new Punto(final_point.getUbicacionX(), this.getUbicacionY(), this.getOrden() + 1, this.getCodigoPedido());
		LocalDateTime[] lapse = new LocalDateTime[] {ini_date, ini_date.plusYears(5)};
		List<Bloqueo> current_locks = AstarFunciones.filter_locks_by_time_span(locks, lapse);
		boolean free_corner1 = !AstarFunciones.hayBloqueosEntre(this, corner1, current_locks) &&
				!AstarFunciones.hayBloqueosEntre(corner1, final_point, current_locks);
		boolean free_corner2 = !AstarFunciones.hayBloqueosEntre(this, corner2, current_locks) &&
				!AstarFunciones.hayBloqueosEntre(corner2, final_point, current_locks);
		return free_corner1? Arrays.asList(corner1): free_corner2? Arrays.asList(corner2):
			AstarFunciones.astarAlgoritmo(this, final_point, current_locks);
		
	}
}
