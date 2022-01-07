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
	private String color;
	List<PuntoDTO>ruta;
	
	public CamionRutaDTO(CamionEstadoDTO dto) {
		this.codigo = dto.getCodigo(); 
		this.ubicacionActualX = dto.getUbicacionActualX(); 
		this.ubicacionActualY = dto.getUbicacionActualY(); 
		this.estadoId = dto.getEstadoId(); 
		this.color = dto.getColor();
		this.estadoNombre = dto.getEstadoNombre(); 
	}
}
