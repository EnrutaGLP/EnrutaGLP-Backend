package com.enrutaglp.backend.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Mantenimiento {
	private int id; 
	private int idCamion; 
	private LocalDateTime fechaInicio; 
	private LocalDateTime fechaFin;
	private byte tipo;
	public Mantenimiento(int idCamion, LocalDateTime fechaInicio, LocalDateTime fechaFin, byte tipo) {
		this.idCamion = idCamion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.tipo = tipo;
	}
}
