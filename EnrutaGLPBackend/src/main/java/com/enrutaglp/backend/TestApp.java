package com.enrutaglp.backend;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Punto;

public class TestApp {
	
	private static List<Bloqueo> generarListaBloqueos(){
		
		List<Bloqueo> bloqueos = new ArrayList<Bloqueo>();
		List<Punto> pEsqBloq = new ArrayList<Punto>();
		
		
		pEsqBloq.add(new Punto (1,7,44));
		pEsqBloq.add(new Punto (1,0,44));
		Bloqueo bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (2,3,44));
		pEsqBloq.add(new Punto (6,3,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (3,6,44));
		pEsqBloq.add(new Punto (3,10,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (5,6,44));
		pEsqBloq.add(new Punto (9,6,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (7,7,44));
		pEsqBloq.add(new Punto (7,10,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (8,1,44));
		pEsqBloq.add(new Punto (8,5,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		return bloqueos;
	}
	
	
	public static void main(String[] args) {
		AstarFunciones af = new AstarFunciones();
		Punto puntoIni = new Punto(0,0,4);
		Punto puntoFinal = new Punto(10,10,2);
		
		List<Bloqueo> bloqueos = generarListaBloqueos();
		LocalDateTime fecha = LocalDateTime.now();
		Camion camion = new Camion("CODIGO",3,4,10.5,5.6);
		
		List<Punto> puntosIntermedios = puntoIni.getPuntosIntermedios(puntoFinal, bloqueos, fecha, camion);
		
//		System.out.println(puntosIntermedios.size());

		af.imprimirCamino(puntosIntermedios, bloqueos);
    }
}
