package com.enrutaglp.backend.dtos;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntregaPedidoDTO {
	private int cantidadEntregada;
	private int id;
	private String codigo; 
	private String cliente; 
	private double cantidadGlp;
	private double cantidadGlpAtendida;
	private double cantidadGlpPorPlanificar;		
	private int ubicacionX;
	private int ubicacionY;
	private LocalDateTime fechaPedido;
	private LocalDateTime fechaLimite;
	private LocalDateTime fechaCompletado;
	private byte estado;
}
