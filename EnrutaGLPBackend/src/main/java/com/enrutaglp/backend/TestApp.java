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
		
		String format = "'v'e'n't'a'syyyyMM'.'t'x'tdd:HH:mm";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern (format);
		
		
		for (int i = 0; i < data.size(); i ++) {
			for (String sale: data.get(i)) {
				
				String[] split = sale.split(",");
				String str = names.get(i) + split[0];
				LocalDateTime datetime = LocalDateTime.parse(str, formatter);
				
				int x = Integer.parseInt(split[1]);
				int y = Integer.parseInt(split[2]);
				int m3 = Integer.parseInt(split[3]);
				int hlim = Integer.parseInt(split[4]);
				
				//Pedido pedido = new Pedido(0,"", "", m3, x, y,null,null,null,0);
			}
			
		}
		
		
    }
}
