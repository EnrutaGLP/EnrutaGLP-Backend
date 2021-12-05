package com.enrutaglp.backend.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActualizacionSimulacionDTO {
	String fechaInicio; 
	String fechaFin;
	List<CamionSimulacionDTO> averiados; 
	List<CamionSimulacionDTO> otros;
	boolean esFinal; 
}
