package com.enrutaglp.backend.models;

import java.time.LocalDateTime;

import com.enrutaglp.backend.enums.TipoRuta;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntregaPedido extends Ruta{
	private double cantidadEntregada;
	private Camion camion;
	private Pedido pedido;

	public EntregaPedido(double cantidadEntregada, LocalDateTime horaLlegada, LocalDateTime horaSalida,
			double consumoPetroleo, Camion camion, Pedido pedido) {
		super(consumoPetroleo,horaLlegada,horaSalida,camion,TipoRuta.ENTREGA.getValue());
		this.cantidadEntregada = cantidadEntregada;
		this.camion = camion;
		this.pedido = pedido;
	}

	public EntregaPedido(double cantidadEntregada, LocalDateTime horaLlegada, LocalDateTime horaSalida,
			double consumoPetroleo) {
		super(consumoPetroleo,horaLlegada,horaSalida,TipoRuta.ENTREGA.getValue());
		this.cantidadEntregada = cantidadEntregada;
	}


}
