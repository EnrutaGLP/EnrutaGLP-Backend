package com.enrutaglp.backend.events;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Ruta;

import lombok.Getter;

@Getter
public class ActualizacionSimulacionEvent extends ApplicationEvent{

	private boolean esFinal;
	private String fechaInicio; 
	private String fechaFin; 
	private List<Bloqueo>bloqueos;
	private Map<Integer,List<Ruta>> rutas; 
	private boolean llegoAlColapso;
	private String codigoPedidoColapso;
	public ActualizacionSimulacionEvent(Object source, boolean esFinal, String fechaInicio, String fechaFin, Map<Integer,List<Ruta>> rutas, boolean llegoAlColapso,String codigoPedidoColapso) {
		super(source);
		this.esFinal = esFinal; 
		this.fechaInicio = fechaInicio; 
		this.fechaFin = fechaFin;
		this.rutas = rutas;
		this.llegoAlColapso = llegoAlColapso; 
		this.codigoPedidoColapso = codigoPedidoColapso; 
	}
	
	public ActualizacionSimulacionEvent(Object source, boolean esFinal, String fechaInicio, String fechaFin, Map<Integer,List<Ruta>> rutas, List<Bloqueo>bloqueos, boolean llegoAlColapso,String codigoPedidoColapso) {
		super(source);
		this.esFinal = esFinal; 
		this.fechaInicio = fechaInicio; 
		this.fechaFin = fechaFin;
		this.rutas = rutas;
		this.bloqueos = bloqueos; 
		this.llegoAlColapso = llegoAlColapso; 
		this.codigoPedidoColapso = codigoPedidoColapso; 
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
