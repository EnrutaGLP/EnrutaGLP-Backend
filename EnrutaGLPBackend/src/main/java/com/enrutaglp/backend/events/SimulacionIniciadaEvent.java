package com.enrutaglp.backend.events;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class SimulacionIniciadaEvent extends ApplicationEvent{

	private byte modoEjecucion; 
	private String fechaInicio; 
	private String fechaFin; 
	public SimulacionIniciadaEvent(Object source, byte modoEjecucion, String fechaInicio, String fechaFin) {
		super(source);
		this.modoEjecucion = modoEjecucion; 
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin; 
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
