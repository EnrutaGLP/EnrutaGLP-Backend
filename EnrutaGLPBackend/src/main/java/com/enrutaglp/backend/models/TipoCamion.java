package com.enrutaglp.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TipoCamion {
	private String tara; 
	private double pesoBruto; 
	private double capacidadGLP; 
	private double pesoGLP; 
	private double pesoCombinado; 
	private double capacidadTanque; 
	private double velocidadPromedio; 
	private int unidades;
}
