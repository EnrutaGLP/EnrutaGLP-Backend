package com.enrutaglp.backend.events;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class UbicacionesActualizadasEvent extends ApplicationEvent{

	

	public UbicacionesActualizadasEvent(Object source) {
		super(source);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
