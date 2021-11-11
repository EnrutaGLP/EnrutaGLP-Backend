package com.enrutaglp.backend.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListaRutasActualesDTO {
	List<CamionRutaDTO>averiados; 
	List<CamionRutaDTO>otros;	
}
