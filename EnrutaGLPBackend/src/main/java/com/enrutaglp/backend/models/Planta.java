package com.enrutaglp.backend.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Planta {
	private int id; 
	private int ubicacionX; 
	private int ubicacionY; 
	private double capacidadPetroleo; 
	private double capacidadGLP;
	private double cargaActualGLP; 
	private double cargaActualPetroleo; 
	private boolean esPrincipal;
}
