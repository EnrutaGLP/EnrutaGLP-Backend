package com.enrutaglp.backend.dtos;

import com.enrutaglp.backend.models.Punto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
