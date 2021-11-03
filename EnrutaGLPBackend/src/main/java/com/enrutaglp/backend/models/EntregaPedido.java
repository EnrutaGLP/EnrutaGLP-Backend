package com.enrutaglp.backend.models;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntregaPedido {
	private double cantidadEntregada;
	private LocalDateTime horaEntregada;
	private LocalDateTime horaSalida;
	private double consumoPetroleo;
	private Camion camion;
	private Pedido pedido;

	public EntregaPedido(double cantidadEntregada, LocalDateTime horaEntregada, LocalDateTime horaSalida,
			double consumoPetroleo, Camion camion, Pedido pedido) {
		this.cantidadEntregada = cantidadEntregada;
		this.horaEntregada = horaEntregada;
		this.horaSalida = horaSalida;
		this.consumoPetroleo = consumoPetroleo;
		this.camion = camion;
		this.pedido = pedido;
	}

	public EntregaPedido(double cantidadEntregada, LocalDateTime horaEntregada, LocalDateTime horaSalida,
			double consumoPetroleo) {
		this.cantidadEntregada = cantidadEntregada;
		this.horaEntregada = horaEntregada;
		this.horaSalida = horaSalida;
		this.consumoPetroleo = consumoPetroleo;
	}


}
