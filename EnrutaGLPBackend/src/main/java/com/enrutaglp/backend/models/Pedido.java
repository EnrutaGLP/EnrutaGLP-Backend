package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
	
	private int id; 
	private String codigo; 
	private String cliente; 
	private double cantidadGlp;
	private double cantidadGlpAtendida;	
	private int ubicacionX;
	private int ubicacionY;
	@JsonIgnore
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	@JsonFormat(
			  shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="America/Lima")
	private LocalDateTime fechaPedido;
	@JsonFormat(
			  shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="America/Lima")
	private LocalDateTime fechaLimite;
	@JsonFormat(
			  shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="America/Lima")
	private LocalDateTime fechaCompletado;
	private byte estado;
	
	
	public Pedido(int id, String codigo, String cliente, double cantidadGlp, double cantidadGlpAtendida, int ubicacionX,
			int ubicacionY, LocalDateTime fechaPedido, LocalDateTime fechaLimite, LocalDateTime fechaCompletado,
			byte estado) {
		this.id = id;
		this.codigo = codigo;
		this.cliente = cliente;
		this.cantidadGlp = cantidadGlp;
		this.cantidadGlpAtendida = cantidadGlpAtendida;
		this.ubicacionX = ubicacionX;
		this.ubicacionY = ubicacionY;
		this.fechaPedido = fechaPedido;
		this.fechaLimite = fechaLimite;
		this.fechaCompletado = fechaCompletado;
		this.estado = estado;
	}
	
	public Pedido(Pedido pedido) {
		this.id = pedido.getId();
		this.codigo = pedido.getCodigo(); 
		this.cliente = pedido.getCliente(); 
		this.cantidadGlpAtendida = pedido.getCantidadGlpAtendida();
		this.cantidadGlp = pedido.getCantidadGlp(); 
		this.ubicacionX = pedido.getUbicacionX(); 
		this.ubicacionY = pedido.getUbicacionY(); 
		this.fechaLimite = pedido.getFechaLimite();
		this.fechaPedido = pedido.getFechaPedido();
		this.fechaCompletado = pedido.getFechaCompletado();
		this.estado = pedido.getEstado();
	}
	
}
