package com.enrutaglp.backend.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuntoSiguienteDTO {
	private Integer id; 
	private Integer ubicacionX;
	private Integer ubicacionY;
	private LocalDateTime siguienteMovimiento;
}
