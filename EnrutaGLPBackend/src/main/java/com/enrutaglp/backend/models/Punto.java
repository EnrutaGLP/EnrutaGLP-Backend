package com.enrutaglp.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Punto {
	private int id;
	private int ubicacionX; 
	private int ubicacionY;
	private int orden; 
	private int idBloqueo;
}
