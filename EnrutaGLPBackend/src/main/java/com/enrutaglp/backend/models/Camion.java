package com.enrutaglp.backend.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Camion {

	private int id; 
	private String codigo; 
	private String placa; 
	private int ubicacionActualX;
	private int ubicacionActualY;	
	private double cargaActualGLP;
	private double cargaActualPetroleo;
	private byte estado;
	private byte tipo;
}
