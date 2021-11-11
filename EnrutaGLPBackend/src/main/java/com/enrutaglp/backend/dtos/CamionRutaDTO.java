package com.enrutaglp.backend.dtos;

import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CamionRutaDTO {
	private String codigo; 
	private int ubicacionActualX;
	private int ubicacionActualY;	
	private byte estadoId; 
	private String estadoNombre;
	private List<PuntoDTO>ruta;
}
