package com.enrutaglp.backend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


import com.enrutaglp.backend.algorithm.AstarFunciones;
import com.enrutaglp.backend.algorithm.FuncionesBackend;
import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Punto;

public class TestApp {
	
	
	
	
	public static void main(String[] args) {
		
		//AstarFunciones.testAstarAlgoritmo();
		//FuncionesBackend.testFuncionesBackend();
		
		String path = "D:\\PUCP\\20141929\\20212\\DP1\\General proyects\\General-proyects\\Data-generator\\output\\";
		
		List<Object> folder = FuncionesBackend.read_folder(path);
		List<String> names = (List<String>)folder.get(0);
		List<List<String>> data = (List<List<String>>) folder.get(1);
		
		String format = "'ventas'yyyyMM'.txt'dd:HH:mm";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern (format);
		
		List<Pedido> pedidos = new ArrayList<Pedido>();
		for (int i = 0; i < data.size(); i ++) {
			for (String sale: data.get(i)) {
				
				String[] split = sale.split(",");
				String str = names.get(i) + split[0];
				int x = Integer.parseInt(split[1]);
				int y = Integer.parseInt(split[2]);
				double m3 = Double.parseDouble(split[3]);
				double hlim = Double.parseDouble(split[4]);
				LocalDateTime fechaPedido = LocalDateTime.parse(str, formatter);
				LocalDateTime fechaLimite = fechaPedido.plusHours((long) hlim);
				LocalDateTime fechaCompletado = LocalDateTime.now();
				
				pedidos.add(new Pedido(0,"", "", m3,(double)0,x,y,
						fechaPedido,fechaLimite,fechaCompletado,(byte) 0));
			}
		}
		
		List<Bloqueo> bloqueos = new ArrayList<Bloqueo>();
		path = "D:\\PUCP\\20141929\\20212\\DP1\\Input\\bloqueos.20211001";
		folder = FuncionesBackend.read_folder(path);
		names = (List<String>)folder.get(0);
		data = (List<List<String>>) folder.get(1);
		format = "yyyyMM'bloqueadas.txt'dd:HH:mm";
		formatter = DateTimeFormatter.ofPattern (format);
		for (int i = 0; i < data.size(); i ++) {
			for (String block: data.get(i)) {
				String[] split = block.split("[,-]+", 3);//Solo se toman las fechas
				String str1 = names.get(i) + split[0];
				String str2 = names.get(i) + split[1];
				LocalDateTime fechaInicio = LocalDateTime.parse(str1, formatter);
				LocalDateTime fechaFin = LocalDateTime.parse(str2, formatter);
				List<Punto> puntos = new ArrayList<Punto>();
				split = split[2].split(",");
				for (int j = 0; j < split.length; j += 2) {
					puntos.add(new Punto (Integer.parseInt(split[j]),
							Integer.parseInt(split[j + 1])));
				}
				bloqueos.add(new Bloqueo (0, fechaInicio, fechaFin, puntos));
			}
		}
    }
}
