package com.enrutaglp.backend;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.enrutaglp.backend.algorithm.AstarFunciones;
import com.enrutaglp.backend.algorithm.FuncionesBackend;
import com.enrutaglp.backend.algorithm.astarAlgoritmo;
import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Punto;

public class TestApp {
	
	
	
	
	public static void main(String[] args) {
		
		List<Bloqueo> bloqueos = generarListaBloqueos();
		LocalDateTime fecha = LocalDateTime.now();
		Camion camion = new Camion("CODIGO",3,4,10.5,5.6);
		
		List<Punto> puntosIntermedios = puntoIni.pruebaAStar(puntoFinal, bloqueos);
		
//		System.out.println(puntosIntermedios.size());

		af.imprimirCamino(puntosIntermedios, bloqueos);
    }
}
