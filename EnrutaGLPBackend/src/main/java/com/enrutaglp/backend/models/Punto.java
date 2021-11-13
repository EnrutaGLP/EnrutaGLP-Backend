package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.enrutaglp.backend.AstarFunciones;

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
	

	
	
	
	public List<Punto> getPuntosIntermedios(Punto puntoFinal, List<Bloqueo> bloqueos, LocalDateTime fechaIni, Camion camion) {
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
		
		if (!af.hayBloqueosEntre (this, pEsquina1, bloqueos, fechaIni, camion) &&
				!af.hayBloqueosEntre (pEsquina1, puntoFinal, bloqueos, fechaIni, camion)) {
//			if (!af.mismaPosicion(puntoFinal, pEsquina1) && !af.mismaPosicion(this, pEsquina1)) {
//				puntosIntemedios.add(pEsquina1);
//			}
			puntosIntemedios.add(pEsquina1);
			return puntosIntemedios;
		}
		
		if (!af.hayBloqueosEntre (this, pEsquina2, bloqueos, fechaIni, camion) &&
				!af.hayBloqueosEntre (pEsquina2, puntoFinal, bloqueos, fechaIni, camion)) {
//			if (!af.mismaPosicion(puntoFinal, pEsquina2) && !af.mismaPosicion(this, pEsquina2)) {
//				puntosIntemedios.add(pEsquina2);
//			}
			puntosIntemedios.add(pEsquina2);
			return puntosIntemedios;
		}
		

		return af.astarAlgoritmo(this, puntoFinal, bloqueos, fechaIni, camion);

	}
	
	public List<Punto> getPuntosIntermedios(Punto puntoFinal, LocalDateTime fechaIni, Camion camion){
		List<Punto> puntosIntemedios = new ArrayList<Punto>();
		
		int sentido = ThreadLocalRandom.current().nextInt(0, 2);
		
		if(sentido==0) {
			//eje x primero
			
			Punto nuevoPunto = new Punto(this.ubicacionX, puntoFinal.getUbicacionY(), this.orden + 1, this.codigoPedido);
			puntosIntemedios.add(nuevoPunto);
		}
		else {
			//eje y primero
			Punto nuevoPunto = new Punto(puntoFinal.getUbicacionX(), this.ubicacionY, this.orden + 1, this.codigoPedido);
			puntosIntemedios.add(nuevoPunto);
		}
		
		return puntosIntemedios;
	}
	
}
