package com.enrutaglp.backend.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	private int id;
	private String nombre; 
	private String correo; 
	private byte idPerfil;
}
