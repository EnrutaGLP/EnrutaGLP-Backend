package com.enrutaglp.backend.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Punto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AStar {
	
	private Punto puntoIni;
	private Punto puntoFin;
	private Camion camion;
	private List<Bloqueo> bloqueos;
	
	public AStar(Punto puntoIni, Punto puntoFin, Camion camion, List<Bloqueo> bloqueosActuales) {
		this.puntoIni = puntoIni;
		this.puntoFin = puntoFin;
		this.camion = camion;
		this.bloqueos = bloqueosActuales;
	}
	
	public List<Punto> run(){
		List<Punto> puntosIntemedios = new ArrayList<Punto>();
		
		
		
		
		return puntosIntemedios;
	}
	
}
