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
			int min_value_to_divide,
			int[] dividers){
		/*
		 * 
		 * If pedido >= min_value_to_divide:
		 * 	map.put (dividir_pedido (pedido, dividers))
		 * 
		 * Example
		 * 
		 * dividers = {15}
		 * If pedido(34) >= min_value_to_divide(25):
		 * 	map.put ({15, 15, 4})
		 *
		 * 
		 */
		Map<String, Pedido> divisions = new HashMap<>();
		
		for (Map.Entry<String, Pedido> entry: pedidos.entrySet()) {
			Pedido pedido = entry.getValue();
			
			if (pedido.getCantidadGlp() >= min_value_to_divide) {

				for (Pedido p:FuncionesBackend.dividirPedido(pedido, dividers)) {
					divisions.put(p.getCodigo(), p);
				}
			}
			else {
				divisions.put(pedido.getCodigo(), pedido);
			}
			
		}
		
		return divisions; 
	}
	
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static double calcularDistanciaTodosPuntos(List<Punto> puntosIntemediosAB) {
		
		double d = 0;
		
		for (int i = 0; i < puntosIntemediosAB.size()-1; i++) {
			d = d + puntosIntemediosAB.get(i).calcularDistanciasNodos(puntosIntemediosAB.get(i+1));
		}
		
		return d;
	}
	
	
}
