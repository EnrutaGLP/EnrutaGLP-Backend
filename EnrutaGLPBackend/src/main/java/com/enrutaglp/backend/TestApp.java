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
		
		
		pEsqBloq.add(new Punto (1,0,44));
		pEsqBloq.add(new Punto (1,2,44));
		Bloqueo bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (0,4,44));
		pEsqBloq.add(new Punto (9,4,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (3,1,44));
		pEsqBloq.add(new Punto (3,3,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (5,0,44));
		pEsqBloq.add(new Punto (5,2,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (7,1,44));
		pEsqBloq.add(new Punto (7,3,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (9,0,44));
		pEsqBloq.add(new Punto (9,2,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (9,5,44));
		pEsqBloq.add(new Punto (9,9,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (7,6,44));
		pEsqBloq.add(new Punto (7,10,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (5,5,44));
		pEsqBloq.add(new Punto (5,9,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		return bloqueos;
	}
	
	
	public static void main(String[] args) {
		AstarFunciones af = new AstarFunciones();
		Punto puntoIni = new Punto(0,0,4);
		Punto puntoFinal = new Punto(4,5,2);
		
		List<Bloqueo> bloqueos = generarListaBloqueos();
		LocalDateTime fecha = LocalDateTime.now();
		Camion camion = new Camion("CODIGO",3,4,10.5,5.6);
		
		List<Punto> puntosIntermedios = puntoIni.getPuntosIntermedios(puntoFinal, bloqueos, fecha, camion);
		
//		System.out.println(puntosIntermedios.size());

		af.imprimirCamino(puntosIntermedios, bloqueos);
    }
}
