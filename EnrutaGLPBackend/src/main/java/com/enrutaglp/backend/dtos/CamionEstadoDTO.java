package com.enrutaglp.backend.dtos;

import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CamionEstadoDTO {
	private String codigo; 
	private int ubicacionActualX;
	private int ubicacionActualY;	
	private String color; 
	private byte estadoId; 
	private String estadoNombre;
}
