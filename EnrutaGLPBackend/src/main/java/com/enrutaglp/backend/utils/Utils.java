package com.enrutaglp.backend.utils;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.enrutaglp.backend.models.Punto;
public class Utils {

	public static String generarCodigoAleatorio(int len) {
		String primeraParte = RandomStringUtils.random(4, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		String segundaParte = RandomStringUtils.random(4, "0123456789");
		return primeraParte + segundaParte;
	}
	
	public static double calcularDistanciaTodosPuntos(List<Punto> puntosIntemediosAB) {
		
		double d = 0;
		
		for (int i = 0; i < puntosIntemediosAB.size()-1; i++) {
			d = d + puntosIntemediosAB.get(i).calcularDistanciasNodos(puntosIntemediosAB.get(i+1));
		}
		
		return d;
	}
	
}
