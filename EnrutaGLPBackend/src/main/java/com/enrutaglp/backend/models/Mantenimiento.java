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
}
