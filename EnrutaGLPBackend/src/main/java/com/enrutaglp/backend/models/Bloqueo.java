package com.enrutaglp.backend.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bloqueo {
	private int id;
	@JsonFormat(
			  shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="America/Lima")
	private Date fechaInicio;
	@JsonFormat(
			  shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="America/Lima")
	private Date fechaFin;
	private List<Punto>puntos;
}
