package com.enrutaglp.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuntoSiguienteDTO {
	private int id; 
	private int ubicacionX;
	private int ubicacionY;
	private boolean isFinal;
}
