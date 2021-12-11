package com.enrutaglp.backend.events;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.enrutaglp.backend.models.Ruta;

import lombok.Getter;

@Getter
public class ActualizacionSimulacionEvent extends ApplicationEvent{

	private boolean esFinal;
	private String fechaInicio; 
	private String fechaFin; 
	private Map<Integer,List<Ruta>> rutas; 
	
	public ActualizacionSimulacionEvent(Object source, boolean esFinal, String fechaInicio, String fechaFin, Map<Integer,List<Ruta>> rutas) {
		super(source);
		this.esFinal = esFinal; 
		this.fechaInicio = fechaInicio; 
		this.fechaFin = fechaFin;
		this.rutas = rutas;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
