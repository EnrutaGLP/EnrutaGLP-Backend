package com.enrutaglp.backend.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.TipoCamion;
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
	
	
	
	public static List<String> get_folder_content (String path, boolean put_name, String sep){
		
		File folder = new File(path);
		List<String> content = new ArrayList<String>();
		
		for (File f: folder.listFiles()) {
			String file_path = path + "\\" + f.getName();
			content.addAll(FuncionesBackend.get_file_content(file_path, put_name, sep));
		}
		return content;
	}
	public static List<String> get_file_content (String path, boolean put_name, String sep){
		try {
			File f = new File(path);
			Scanner reader = new Scanner(f);
			List<String> content = new ArrayList<String>();
			
			while (reader.hasNextLine()) {
				
				content.add((put_name? f.getName() + sep:"") + reader.nextLine());
			}
			reader.close();
			return content;
		}
		catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException exception occurred.");
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static List<Pedido> get_sales (List<String> content){
		List<Pedido> sales = new ArrayList<Pedido>();
		for (String line: content) {
			sales.add(new Pedido(line));
		}
		return sales;
	}
	
	public static List<Bloqueo> get_locks (List<String> content){
		List<Bloqueo> locks = new ArrayList<Bloqueo>();
		for (String line: content) {
			locks.add(new Bloqueo(line));
		}
		return locks;
	}
	
	public static List<Mantenimiento> get_maintenances (List<String> content){
		List<Mantenimiento> maintenances = new ArrayList<Mantenimiento>();
		String format = "yyyy-MM-ddHH:mm:ss";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern (format);
		
		for (String line: content) {
			String[] split = line.split(",");
			
			LocalDateTime ini_date = LocalDateTime.parse(split[2]+split[3], formatter);
			LocalDateTime final_date = LocalDateTime.parse(split[4]+split[5], formatter);
			maintenances.add(new Mantenimiento(Integer.parseInt(split[1]),ini_date, final_date, Byte.parseByte(split[6])));
			
		}
		return maintenances;
	}
	
	public static List<TipoCamion> get_truck_types (List<String> content){
		List<TipoCamion> truck_types = new ArrayList<TipoCamion>();
		for (String line: content) {
			String[] split = line.split("\\t");
			TipoCamion truck_type = new TipoCamion(split[1],Double.parseDouble(split[2]),Double.parseDouble(split[3]),Double.parseDouble(split[4]),
					Double.parseDouble(split[5]),Double.parseDouble(split[6]),Double.parseDouble(split[7]),Integer.parseInt(split[8]));
			truck_types.add(truck_type);
			
		}
		return truck_types ;
	}
	
	public static List<Camion> get_trucks (List<String> content,List<TipoCamion> truck_types){
		List<Camion> trucks = new ArrayList<Camion>();
		
		for (String line: content) {
			String[] split = line.split("\\t");
			trucks.add(new Camion(Integer.parseInt(split[0]),split[1],split[2],Integer.parseInt(split[3]),Integer.parseInt(split[4]),
					Double.parseDouble(split[5]),Double.parseDouble(split[6]), Byte.parseByte(split[7]), truck_types.get(Integer.parseInt(split[8]) - 1)));
		}
		return trucks;
	}
}
