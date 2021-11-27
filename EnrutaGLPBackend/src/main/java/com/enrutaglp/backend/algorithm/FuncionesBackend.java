package com.enrutaglp.backend.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.utils.Utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FuncionesBackend {
	
	public static int[] capacidadGLP = {25,15,10,5};
	
	
	public static List<Pedido> dividirPedido(Pedido pADividir, int maxDivisor){
		
		double restoGlp = pADividir.getCantidadGlp();
		List<Pedido> pDivididos = new ArrayList<Pedido>();
		int indice = 1;
		for (int cap: capacidadGLP) {
			if (cap > maxDivisor) {
				continue;
			}
			int cociente = (int)(restoGlp/cap);
			for (int i=0; i<cociente; i++) {
				pDivididos.add(new Pedido(
						pADividir,
						pADividir.getCodigo() + " - " + String.valueOf(indice++),
						cap));
			}
			restoGlp = restoGlp%cap;
		}
		if (restoGlp > 0) {
			pDivididos.add(new Pedido(
					pADividir,
					pADividir.getCodigo() + " - " + String.valueOf(indice++),
					restoGlp));
		}
		return pDivididos;
	}
	
	
	public static void testFuncionesBackend () {
		
		Map<String, Pedido> pedidos = new HashMap<>();
		Pedido p = new Pedido (1,"", "cliente", 0,0,0,0,
				LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now(),Byte.MAX_VALUE);
		
		int[]cargasGLP = {1};
		for (int i = 0;i < cargasGLP.length; i++) {
			p.setCantidadGlp(cargasGLP[i]);
			p.setCodigo("COD" + String.valueOf(i + 1));
			pedidos.put(p.getCodigo(), new Pedido(p));
		}
		
		
		pedidos = Utils.particionarPedidos(pedidos,5,5);
		for (Map.Entry<String, Pedido> entry: pedidos.entrySet()) {
			System.out.print(entry.getKey()+", GLP: ");
			System.out.println(entry.getValue().getCantidadGlp());
		}
	}
	
	
	
	public static List<Object> read_folder (String path){
		try {	
			List<List<String>> data = new ArrayList<List<String>>();
			File folder = new File(path);
			List <String> names = new ArrayList<String>();
			List<Object> output = new ArrayList<Object>();
			
			for (File f: folder.listFiles()) {
				Scanner reader = new Scanner(f);
				
				List<String> lines = new ArrayList<String>();
				
				while (reader.hasNextLine()) {
					
					lines.add(reader.nextLine());
				}
				reader.close();
				
				data.add(lines);
				names.add(f.getName());
			}
			output.add(names);
			output.add(data);
			return output;
		}
		catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException exception occurred.");
			e.printStackTrace();
			
			return null;
		}
	}
}
