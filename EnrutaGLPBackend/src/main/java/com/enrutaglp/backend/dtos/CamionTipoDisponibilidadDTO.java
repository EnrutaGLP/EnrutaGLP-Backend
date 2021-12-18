package com.enrutaglp.backend.dtos;

import java.time.LocalDateTime;

import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.TipoCamion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CamionTipoDisponibilidadDTO {
	private int id;
	private String codigo; 
	private String placa; 
	private int ubicacionActualX;
	private int ubicacionActualY;
	private double cargaActualGlp; 
	private double cargaActualPetroleo; 
	private byte estado;
	private byte tipo;
	private int tipoCamionId; 
	private String tara; 
	private double pesoBruto; 
	private double capacidadGlp;
	private double pesoGlp; 
	private double pesoCombinado; 
	private double capacidadTanque;
	private double velocidadPromedio; 
	private int unidades;
	private LocalDateTime horaLlegada;
	
	public Camion toModel() {
		return new Camion(id,codigo,placa,ubicacionActualX,ubicacionActualY,cargaActualGlp,cargaActualPetroleo,
				estado,new TipoCamion(tara, pesoBruto, capacidadGlp, pesoGlp, pesoCombinado, capacidadTanque, velocidadPromedio, unidades));
	}
}
