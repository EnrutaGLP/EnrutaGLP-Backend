package com.enrutaglp.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuntoSiguienteDTO {
	private Integer id; 
	private Integer ubicacionX;
	private Integer ubicacionY;
	private Boolean isFinal;
}
