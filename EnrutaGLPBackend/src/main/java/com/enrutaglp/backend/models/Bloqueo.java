package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Bloqueo {
	private int id;
	@JsonFormat(
			  shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="America/Lima")
	private LocalDateTime fechaInicio;
	@JsonFormat(
			  shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="America/Lima")
	private LocalDateTime fechaFin;
	private List<Punto>puntos;
	
	public Bloqueo(int id, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.puntos = new ArrayList<Punto>();
	}
	
	public Bloqueo(int id, LocalDateTime fechaInicio, LocalDateTime fechaFin,List<Punto>puntos) {
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.puntos = puntos;
	}
	
	
	public Bloqueo (String line) {
		/*
		 * The format of the line can be
		 * 
		 * yyyyMMbloqueadas.txt,dd:HH:mm,fechaInicio-fechaFin,puntos
		 * 
		 * Examples of line
		 * 
		 * 202110bloqueadas.txt,01:00:15-01:06:16,5,20,5,35
		 * 202110bloqueadas.txt,01:03:31-01:12:09,25,30,30,30,30,20,40,20,40,10
		 * 202110bloqueadas.txt,01:05:08-01:13:20,35,30,35,25,45,25
		 */
		
		String format = "yyyyMM'bloqueadas.txt'dd:HH:mm";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern (format);
		
		String[] split = line.split("[,-]+");
		String str1 = split[0] + split[1];
		String str2 = split[0] + split[2];
		LocalDateTime fechaInicio = LocalDateTime.parse(str1, formatter);
		LocalDateTime fechaFin = LocalDateTime.parse(str2, formatter);
		List<Punto> puntos = new ArrayList<Punto>();
		
		for (int i = 3; i < split.length; i += 2) {
			puntos.add(new Punto (Integer.parseInt(split[i]),
					Integer.parseInt(split[i + 1])));
		}
		//this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.puntos = puntos;
	}
}
