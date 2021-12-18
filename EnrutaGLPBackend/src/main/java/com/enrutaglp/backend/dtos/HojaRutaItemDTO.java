package com.enrutaglp.backend.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.enrutaglp.backend.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HojaRutaItemDTO {
	private int id;
	private String codigoCamion; 
	private LocalDateTime horaSalida; 
	private LocalDateTime horaLlegada; 
	private Double consumoPetroleo; 
	private List<PuntoDTO> puntos; 
	private int tipo; 
	private String codigoPedido; 
	private Double cantidadEntregada; 
	private Double cantidadGlp; 
	private LocalDateTime fechaLimite; 
	private String nombrePlanta;
	private Integer cantidadRecargada;
	
	public HojaRutaItemDTO(HojaRutaItemSinPuntosDTO hr, List<PuntoDTO> puntos) {
		this.id = hr.getId(); 
		this.codigoCamion = hr.getCodigoCamion(); 
		this.horaSalida = hr.getHoraSalida(); 
		this.horaLlegada = hr.getHoraLlegada(); 
		this.consumoPetroleo = Utils.round(hr.getConsumoPetroleo(), 2); 
		this.puntos = puntos; 
		this.tipo = hr.getTipo(); 
		this.codigoPedido = hr.getCodigoPedido(); 
		this.cantidadEntregada = hr.getCantidadEntregada(); 
		this.cantidadGlp = hr.getCantidadGlp(); 
		this.fechaLimite = hr.getFechaLimite(); 
		this.nombrePlanta = hr.getNombrePlanta(); 
		this.cantidadRecargada = hr.getCantidadRecargada();
	}
	
}
