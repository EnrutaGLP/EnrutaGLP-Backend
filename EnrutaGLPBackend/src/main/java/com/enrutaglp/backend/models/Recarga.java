package com.enrutaglp.backend.models;

import java.time.LocalDateTime;

import com.enrutaglp.backend.enums.TipoRuta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recarga extends Ruta{
	private double cantidadRecargada;
	
	public Recarga() {
		
	}
	
	public Recarga(Punto pInicial, Punto pFinal, Camion camion, LocalDateTime horaSalida, int orden, double cantidadRecargada) {
		super(pInicial,pFinal,camion,horaSalida,orden,TipoRuta.RECARGA.getValue());
		this.cantidadRecargada = 0;
	}
	
	
	public Recarga(double consumoPetroleo, double cantidadRecargada, Camion camion, LocalDateTime horaLlegada, LocalDateTime horaSalida) {
		super(consumoPetroleo, horaLlegada, horaSalida,camion,TipoRuta.RECARGA.getValue());
		this.cantidadRecargada = cantidadRecargada;
	}
	
	public Recarga(Punto puntoIni, LocalDateTime horasalida, int orden, Camion camion) {
		super(puntoIni, horasalida, orden, camion);
		cantidadRecargada = 0;
	}
	
	@Override
	public Recarga clone() {
		Recarga p = new Recarga();

		p.setConsumoPetroleo(this.getConsumoPetroleo());
		p.setOrden(this.getOrden());
		p.setPuntos(this.getPuntos());
		p.setDistanciaRecorrida(this.getDistanciaRecorrida());
		p.setCostoRuta(this.getCostoRuta());
		p.setHoraLlegada(this.getHoraLlegada());
		p.setHoraSalida(this.getHoraSalida());
		p.setCamion(this.getCamion());
		p.setTipo(this.getTipo());
		
		p.cantidadRecargada = this.cantidadRecargada;
		return p; 
	}
	
}
