package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ruta {
	private double consumoPetroleo; 
	private int orden; 
	private List<Punto>puntos;
	private LocalDateTime horaLlegada;
	private LocalDateTime horaSalida;
	private Camion camion;
	private byte tipo;
	
	public Ruta(double consumoPetroleo, LocalDateTime horaLlegada, LocalDateTime horaSalida,byte tipo) {
		this.consumoPetroleo = consumoPetroleo;
		this.horaLlegada = horaLlegada;
		this.horaSalida = horaSalida;
		this.tipo = tipo; 
	}
	
	public Ruta(double consumoPetroleo, LocalDateTime horaLlegada, LocalDateTime horaSalida,Camion camion, byte tipo) {
		this.consumoPetroleo = consumoPetroleo;
		this.horaLlegada = horaLlegada;
		this.horaSalida = horaSalida;
		this.camion = camion;
		this.tipo = tipo; 
	}
	
}
