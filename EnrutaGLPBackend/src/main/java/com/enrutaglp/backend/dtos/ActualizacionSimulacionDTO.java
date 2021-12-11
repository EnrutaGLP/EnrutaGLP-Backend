package com.enrutaglp.backend.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.enrutaglp.backend.models.Bloqueo;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActualizacionSimulacionDTO {
	private String fechaInicio; 
	private String fechaFin;
	//private double porcentajePlazoOcupadoPromedio;
	private List<CamionSimulacionDTO> averiados; 
	private List<CamionSimulacionDTO> otros;
	private List<Bloqueo> bloqueos;
	boolean esFinal; 
}
