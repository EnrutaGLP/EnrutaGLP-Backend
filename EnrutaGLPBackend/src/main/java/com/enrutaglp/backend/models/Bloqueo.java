package com.enrutaglp.backend.models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bloqueo {
	private int id;
	private Date fechaInicio;
	private Date fechaFin;
}
