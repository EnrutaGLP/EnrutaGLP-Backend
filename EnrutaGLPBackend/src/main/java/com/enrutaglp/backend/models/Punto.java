package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.enrutaglp.backend.algorithm.AstarFunciones;
import com.enrutaglp.backend.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore
	private String codigoPedido;
	@Setter
	@JsonIgnore
	private boolean planta;
	@Setter
	@JsonIgnore
	private double astarF;
	@Setter
	@JsonIgnore
	private double astarG;
	@Setter
	@JsonIgnore
	private double astarH;
	@Setter
	@JsonIgnore
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
		
		AstarFunciones af = new AstarFunciones();
		
		List<Punto> puntosIntemedios = new ArrayList<Punto>();
		
		puntosIntemedios.add(this);
		puntosIntemedios.add(puntoFinal);
		
		if(this.ubicacionX!=puntoFinal.getUbicacionX() && this.ubicacionY!=puntoFinal.getUbicacionY()){
			Punto nuevoPunto1 = new Punto(this.ubicacionX, puntoFinal.getUbicacionY(), this.orden + 1, this.codigoPedido);
			puntosIntemedios.add(1,nuevoPunto1);			
		}

		double distanciaPuntosActual = Utils.calcularDistanciaTodosPuntos(puntosIntemedios);
		long tiempo = (long) (distanciaPuntosActual/camion.getTipo().getVelocidadPromedio() * 3600);
		LocalDateTime fechaHoraEntrega = fechaIni.plusSeconds(tiempo);
		
		
		LocalDateTime[] lapse = new LocalDateTime[] {fechaIni, fechaHoraEntrega};
		List<Bloqueo> bloqueosActuales = AstarFunciones.filter_locks_by_time_span(bloqueos, lapse);
		
		
		if(bloqueosActuales.size()==0 || !af.hayBloqueosEnMiCamino(puntosIntemedios, bloqueosActuales)) {
			puntosIntemedios.remove(0);
			puntosIntemedios.remove(puntosIntemedios.size()-1);
			return puntosIntemedios;
		}
		
		puntosIntemedios.clear();
		puntosIntemedios.add(this);
		puntosIntemedios.add(puntoFinal);
		
		if(this.ubicacionX!=puntoFinal.getUbicacionX() && this.ubicacionY!=puntoFinal.getUbicacionY()) {
			Punto nuevoPunto2 = new Punto(puntoFinal.getUbicacionX(), this.ubicacionY, this.orden + 1, this.codigoPedido);
			puntosIntemedios.add(1,nuevoPunto2);			
		}
		
		if(bloqueosActuales.size()==0 || !af.hayBloqueosEnMiCamino(puntosIntemedios, bloqueosActuales)) {
			puntosIntemedios.remove(0);
			puntosIntemedios.remove(puntosIntemedios.size()-1);
			return puntosIntemedios;
		}
		
		
		puntosIntemedios.clear();
		
		//List<Bloqueo> bloqueosActuales = bloqueosActuales1.size()<bloqueosActuales2.size() ?  bloqueosActuales1 : bloqueosActuales2;
		distanciaPuntosActual = 0;
		tiempo = 0;
		fechaHoraEntrega = null;
		
		int numBloqueos = 0;
		
		while(bloqueosActuales.size()>numBloqueos) {
			//AStar aStar = new AStar(this, puntoFinal, camion, bloqueosActuales);
			//AstarFunciones af = new AstarFunciones();
			puntosIntemedios.clear();
			puntosIntemedios = af.astarAlgoritmo(this, puntoFinal, bloqueosActuales);
			
			if(puntosIntemedios == null) {
				puntosIntemedios = new ArrayList<Punto>();
			} 
			
			puntosIntemedios.add(0, this);
			puntosIntemedios.add(puntoFinal);
			
			distanciaPuntosActual = Utils.calcularDistanciaTodosPuntos(puntosIntemedios);
			tiempo = (long) (distanciaPuntosActual/camion.getTipo().getVelocidadPromedio() * 3600);
			fechaHoraEntrega = fechaIni.plusSeconds(tiempo);
			
			numBloqueos = bloqueosActuales.size();
			
			bloqueosActuales.clear();
			bloqueosActuales = AstarFunciones.filter_locks_by_time_span(bloqueos, new LocalDateTime[] {fechaIni, fechaHoraEntrega});
		}
		
		puntosIntemedios.remove(0);
		puntosIntemedios.remove(puntosIntemedios.size()-1);
		return puntosIntemedios;
	}
	
	
	public List<Punto> getWayTo (Punto final_point, LocalDateTime ini_date, Camion truck, List<Bloqueo> locks){
		
		
		Punto corner1 = new Punto(this.getUbicacionX(), final_point.getUbicacionY(), this.getOrden(), this.getCodigoPedido());
		Punto corner2 = new Punto(final_point.getUbicacionX(), this.getUbicacionY(), this.getOrden(), this.getCodigoPedido());
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
