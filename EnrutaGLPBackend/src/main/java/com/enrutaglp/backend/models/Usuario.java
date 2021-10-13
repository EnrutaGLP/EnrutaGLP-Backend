package com.enrutaglp.backend.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
	private int id;
	private String nombre; 
	private String correo; 
	private byte idPerfil;
}
