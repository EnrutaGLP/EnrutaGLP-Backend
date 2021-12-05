package com.enrutaglp.backend.dtos;

import com.enrutaglp.backend.models.Punto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuntoDTO {
	private Integer ubicacionX; 
	private Integer ubicacionY;
	private Integer orden;
	
	public PuntoDTO(Punto punto) {
		this.ubicacionX = punto.getUbicacionX(); 
		this.ubicacionY = punto.getUbicacionY(); 
		this.orden = punto.getOrden();
	}
}
