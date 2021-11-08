package com.enrutaglp.backend.models;

import java.time.LocalDateTime;

import com.enrutaglp.backend.enums.TipoRuta;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntregaPedido extends Ruta{
	private double cantidadEntregada;
	private Pedido pedido;

	public EntregaPedido() {
		
	}
	
	public EntregaPedido(Punto pInicial, Punto pFinal, Camion camion, LocalDateTime horaSalida, int orden, double cantidadEntregada, Pedido pedido) {
		super(pInicial,pFinal,camion,horaSalida,orden);
		this.cantidadEntregada = cantidadEntregada;
		this.pedido = pedido;
	}
	
	public EntregaPedido(double cantidadEntregada, LocalDateTime horaLlegada, LocalDateTime horaSalida,
			double consumoPetroleo, Camion camion, Pedido pedido) {
		super(consumoPetroleo,horaLlegada,horaSalida,camion,TipoRuta.ENTREGA.getValue());
		this.cantidadEntregada = cantidadEntregada;
		this.pedido = pedido;
	}

	public EntregaPedido(double cantidadEntregada, LocalDateTime horaLlegada, LocalDateTime horaSalida,
			double consumoPetroleo) {
		super(consumoPetroleo,horaLlegada,horaSalida,TipoRuta.ENTREGA.getValue());
		this.cantidadEntregada = cantidadEntregada;
	}

	@Override
	public Ruta clone() {
		//EntregaPedido p = (EntregaPedido) super.clone();
		EntregaPedido p = new EntregaPedido();
		
		p.setConsumoPetroleo(this.getConsumoPetroleo());
		p.setOrden(this.getOrden());
		p.setPuntos(this.getPuntos());
		p.setDistanciaRecorrida(this.getDistanciaRecorrida());
		p.setCostoRuta(this.getCostoRuta());
		p.setHoraLlegada(this.getHoraLlegada());
		p.setHoraSalida(this.getHoraSalida());
		p.setCamion(this.getCamion());
		p.setTipo(this.getTipo());
			
		p.cantidadEntregada = this.cantidadEntregada;
		p.pedido = this.pedido;

		return p; 
	}

}
