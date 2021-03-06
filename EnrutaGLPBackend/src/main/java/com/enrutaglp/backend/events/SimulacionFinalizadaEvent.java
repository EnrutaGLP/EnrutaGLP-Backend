package com.enrutaglp.backend.events;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class SimulacionFinalizadaEvent extends ApplicationEvent{

	private byte modoEjecucion; 
	
	public SimulacionFinalizadaEvent(Object source, byte modoEjecucion) {
		super(source);
		this.modoEjecucion = modoEjecucion; 
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
