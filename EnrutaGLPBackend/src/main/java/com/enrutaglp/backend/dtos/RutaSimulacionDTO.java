package com.enrutaglp.backend.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Setter
@AllArgsConstructor
public class RutaSimulacionDTO {
	String codigoPedido; 
	LocalDateTime horaSalida; 
	List<PuntoDTO> puntos;
}
