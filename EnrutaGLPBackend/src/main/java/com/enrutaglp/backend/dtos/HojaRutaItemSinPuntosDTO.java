package com.enrutaglp.backend.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HojaRutaItemSinPuntosDTO {
	private int id;
	private String codigoCamion; 
	private LocalDateTime horaSalida; 
	private LocalDateTime horaLlegada; 
	private Double consumoPetroleo; 
	private int tipo; 
	private String codigoPedido; 
	private Double cantidadEntregada; 
	private Double cantidadGlp; 
	private LocalDateTime fechaLimite; 
	private String nombrePlanta;
	private Integer cantidadRecargada;
	
}
