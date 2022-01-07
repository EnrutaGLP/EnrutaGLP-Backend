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
public class Pedido implements Comparable<Pedido> {
	
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
	
	public Pedido(Pedido pedido, String codigo, double cantidadGlp) {
		this.id = pedido.getId();
		this.codigo = codigo; 
		this.cliente = pedido.getCliente(); 
		this.cantidadGlpAtendida = pedido.getCantidadGlpAtendida();
		this.cantidadGlp = cantidadGlp; 
		this.ubicacionX = pedido.getUbicacionX(); 
		this.ubicacionY = pedido.getUbicacionY(); 
		this.fechaLimite = pedido.getFechaLimite();
		this.fechaPedido = pedido.getFechaPedido();
		this.fechaCompletado = pedido.getFechaCompletado();
		this.estado = pedido.getEstado();
	}
	
	
	public Pedido (String line, String cod) {
		/*
		 * The format of the line can be
		 * 
		 * ventasyyyyMM.txt,dd:HH:mm,posX,posY,m3,hLim
		 * 
		 * Examples of line
		 * 
		 * ventas202111.txt,16:00:46,69,10,13,14
		 * ventas202111.txt,16:05:46,60,15,35,18
		 * ventas202111.txt,16:06:46,2,5,1,8
		 */
		
		String format = "'ventas'yyyyMM'.txt'dd:HH:mm";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern (format);
		String[] split = line.split(",");
		
		String str = split[0] + split[1];
		int x = Integer.parseInt(split[2]);
		int y = Integer.parseInt(split[3]);
		double m3 = Double.parseDouble(split[4]);
		double hlim = Double.parseDouble(split[5]);
		LocalDateTime fechaPedido = LocalDateTime.parse(str, formatter);
		LocalDateTime fechaLimite = fechaPedido.plusHours((long) hlim);
		LocalDateTime fechaCompletado = LocalDateTime.now();
		
		//this.id = id;
		this.codigo = cod;
		//this.cliente = cliente;
		this.cantidadGlp = m3;
		//this.cantidadGlpAtendida = cantidadGlpAtendida;
		this.ubicacionX = x;
		this.ubicacionY = y;
		this.fechaPedido = fechaPedido;
		this.fechaLimite = fechaLimite;
		this.fechaCompletado = fechaCompletado;
		//this.estado = estado;
		
	}

	@Override
	public int compareTo(Pedido o) {
		if(this.fechaPedido.isAfter(o.getFechaPedido())) {
			return 1; 
		}else {
			return -1;
		}
	}
	
	public String to_string () {
		
		String str = "";
		str.concat("Cliente: " + this.cliente + "\n");
		str.concat("Cantidad glp: " + this.cantidadGlp + "\n");
		str.concat("Cantidad glp atendida: " + this.cantidadGlpAtendida + "\n");
		str.concat("Ubicación: (" + this.ubicacionX + ", " + this.ubicacionY + ")\n\n");
		return str;
	}
}
