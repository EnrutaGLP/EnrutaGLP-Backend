package com.enrutaglp.backend.models;

import java.time.LocalDateTime;

import com.enrutaglp.backend.enums.TipoRuta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recarga extends Ruta{
	private double cantidadRecargada;
	
	public Recarga(double consumoPetroleo, double cantidadRecargada, Camion camion, LocalDateTime horaLlegada, LocalDateTime horaSalida) {
		super(consumoPetroleo, horaLlegada, horaSalida,camion,TipoRuta.RECARGA.getValue());
		this.cantidadRecargada = cantidadRecargada;
	}
}
