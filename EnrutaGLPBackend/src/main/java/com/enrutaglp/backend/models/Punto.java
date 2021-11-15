package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.enrutaglp.backend.algorithm.AStar;
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
	
	
	//public List<Punto> getPuntosIntermedios(Punto puntoFinal, List<Bloqueo> bloqueos, LocalDateTime fechaIni, Camion camion) {
		
		//List<Punto> puntosIntemedios = new ArrayList<Punto>();
		
		/*
		this.distanciaRecorrida = this.puntos.get(this.puntos.size()-2).calcularDistanciasNodos(this.puntos.get(this.puntos.size()-1));
		int tiempo = (int) (this.distanciaRecorrida/this.camion.getTipo().getVelocidadPromedio());
		fechafinal = fechaIni.plusHours(tiempo);
		*/
		//verificar fechaini y fechafin, si no hay bloqueos devolver listapuntos
		
		//si hay bloqueos A *
		
		//return puntosIntemedios;
	//}
	
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
		
		while(bloqueosActuales.size()>0) {
			AStar aStar = new AStar(this, puntoFinal, camion, bloqueosActuales);
			puntosIntemedios = aStar.run();
			distanciaPuntosActual = Utils.calcularDistanciaTodosPuntos(puntosIntemedios);
			tiempo = (long) (distanciaPuntosActual/camion.getTipo().getVelocidadPromedio() * 3600);
			fechaHoraEntrega = fechaIni.plusSeconds(tiempo);
			
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

	
}
