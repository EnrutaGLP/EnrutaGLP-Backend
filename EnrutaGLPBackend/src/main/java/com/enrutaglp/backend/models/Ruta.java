package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ruta {
	private double consumoPetroleo; 
	private int orden; 
	private List<Punto>puntos;
	private double distanciaRecorrida;
	private double costoRuta;
	private LocalDateTime horaLlegada;
	private LocalDateTime horaSalida;
	private Camion camion;
	private byte tipo;
	
	public Ruta clone() {
		Ruta p = new Ruta(); 

		return p; 
	}

	public void setNodos(List<Punto> nodos) {
		this.puntos = new ArrayList<Punto>();
		Iterator<Punto> iterator = nodos.iterator();
		 
		while(iterator.hasNext())
		{
			this.puntos.add(((Punto)iterator.next()).clone());  
		}
	}
	
	public Ruta() {

	}
	
	public Ruta(Punto pInicial, Punto pFinal, Camion camion, LocalDateTime horaSalida, int orden, byte tipo) {
		this.puntos = new ArrayList<Punto>();
		this.puntos.add(pInicial);
		this.puntos.add(pFinal);
		this.camion = camion;
		this.horaSalida = horaSalida;
		this.orden = orden;
		
		this.consumoPetroleo = 0;
		this.distanciaRecorrida = 0;
		this.costoRuta = 0;
		
		this.tipo = tipo;
		this.calcularVariables();
	}
	
	public void calcularVariables() {
		this.distanciaRecorrida = this.puntos.get(this.puntos.size()-2).calcularDistanciasNodos(this.puntos.get(this.puntos.size()-1));
		int tiempo = (int) (this.distanciaRecorrida/this.camion.getTipo().getVelocidadPromedio());
		this.horaLlegada = this.horaSalida.plusHours(tiempo);
	}
	
		
	public Ruta(Punto puntoIni, LocalDateTime horasalida, int orden, Camion camion) {
		this.puntos = new ArrayList<Punto>();
		
		this.puntos.add(puntoIni);
		this.horaSalida = horasalida;
		this.orden = orden;
		this.camion = camion;
		this.consumoPetroleo = 0;
		this.distanciaRecorrida = 0;
		this.costoRuta = 0;
		
	}
	
	public Ruta(double consumoPetroleo, LocalDateTime horaLlegada, LocalDateTime horaSalida,byte tipo) {
		this.consumoPetroleo = consumoPetroleo;
		this.horaLlegada = horaLlegada;
		this.horaSalida = horaSalida;
		this.tipo = tipo; 
		this.puntos = new ArrayList<Punto>();
	}
	
	public Ruta(double consumoPetroleo, LocalDateTime horaLlegada, LocalDateTime horaSalida,Camion camion, byte tipo) {
		this.consumoPetroleo = consumoPetroleo;
		this.horaLlegada = horaLlegada;
		this.horaSalida = horaSalida;
		this.camion = camion;
		this.tipo = tipo; 
		this.puntos = new ArrayList<Punto>();
	}
	
}
