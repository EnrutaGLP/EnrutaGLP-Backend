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
		
		String path = "D:\\PUCP\\20141929\\20212\\DP1\\my_input\\sales";
		
		List<Object> folder = FuncionesBackend.read_folder(path);
		List<String> names = (List<String>)folder.get(0);
		List<List<String>> data = (List<List<String>>) folder.get(1);
		
		
		List<Pedido> pedidos = new ArrayList<Pedido>();
		for (int i = 0; i < data.size(); i ++) {
			for (String line: data.get(i)) {
				
				pedidos.add(new Pedido(names.get(i) + "," + line));
			}
		}
		
		List<Bloqueo> bloqueos = new ArrayList<Bloqueo>();
		path = "D:\\PUCP\\20141929\\20212\\DP1\\my_input\\locks";
		folder = FuncionesBackend.read_folder(path);
		names = (List<String>)folder.get(0);
		data = (List<List<String>>) folder.get(1);
		
		for (int i = 0; i < data.size(); i ++) {
			for (String line: data.get(i)) {
				
				bloqueos.add(new Bloqueo (names.get(i) + "," + line));
			}
		}
		
		
		
		Punto ini_point = new Punto(0,0);
		Punto final_point = new Punto (pedidos.get(0).getUbicacionX(), pedidos.get(0).getUbicacionY());
		AstarFunciones.altoTabla = 11;
		AstarFunciones.anchoTabla = 11;

		List<Punto> intermediates = ini_point.getWayTo(final_point, pedidos.get(0).getFechaPedido(), bloqueos);
		AstarFunciones.imprimirCamino(intermediates, bloqueos);
    }
}
