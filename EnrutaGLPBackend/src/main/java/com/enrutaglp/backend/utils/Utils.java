package com.enrutaglp.backend.utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
		 * Example 2
		 * 
		 * 70 -> 15 15 15 15 10
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
	
	public static LocalDateTime obtenerFechaLimiteMax(List<Pedido> pedidos) {
    	LocalDateTime fechaLimiteMaximo = null;
    	
    	for(Pedido p: pedidos) {
    		if(fechaLimiteMaximo == null) {
				fechaLimiteMaximo = p.getFechaLimite(); 
			} else if(fechaLimiteMaximo.isBefore(p.getFechaLimite())) {
				fechaLimiteMaximo = p.getFechaLimite(); 
			}
    	}
    	
    	return fechaLimiteMaximo; 
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
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
}
