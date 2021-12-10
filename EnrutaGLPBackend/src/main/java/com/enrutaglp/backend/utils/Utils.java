package com.enrutaglp.backend.utils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

import com.enrutaglp.backend.algorithm.AstarFunciones;
import com.enrutaglp.backend.algorithm.FuncionesBackend;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Punto;
public class Utils {

	public static String generarCodigoAleatorio(int len) {
		String primeraParte = RandomStringUtils.random(4, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		String segundaParte = RandomStringUtils.random(4, "0123456789");
		return primeraParte + segundaParte;
	}
	
	public static LocalDateTime obtenerFechaHoraActual() {
		return LocalDateTime.now(ZoneId.of("America/Lima"));
	}
	
	public static Map<String, Pedido> particionarPedidos(
			Map<String,Pedido>pedidos,
			int maxTope,
			int maxDivisor){
		/*
		 * Para cada pedido se verifica que este no supere el máximo glp que un camion
		 * de la flota pueda transportar. Si algun pedido supera el máximo permitido será
		 * dividido en varios pedidos más pequeños.
		 * Ejemplo para un pedido dado:
		 * GLP pedido: 73
		 * Salida esperada:
		 * Pedidos de GLP de 20, 20, 20, 10 y 3
		 * 
		 */
		Map<String, Pedido> pParticionados = new HashMap<>();
		
		for (Map.Entry<String, Pedido> entry: pedidos.entrySet()) {
			Pedido pedido = entry.getValue();
			
			if (pedido.getCantidadGlp() > maxTope) {
				/* 
				 * Solo se dividen aquellos pedidos > maxDivisor
				 */
				for (Pedido p:FuncionesBackend.dividirPedido(pedido,maxDivisor)) {
					pParticionados.put(p.getCodigo(), p);
				}
			}
			else {
				pParticionados.put(pedido.getCodigo(), pedido);
			}
			
		}
		
		return pParticionados; 
	}
	
	public static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	
	public static double calcularDistanciaTodosPuntos(List<Punto> puntosIntemediosAB) {
		
		double d = 0;
		
		for (int i = 0; i < puntosIntemediosAB.size()-1; i++) {
			d = d + puntosIntemediosAB.get(i).calcularDistanciasNodos(puntosIntemediosAB.get(i+1));
		}
		
		return d;
	}
	
	
}
