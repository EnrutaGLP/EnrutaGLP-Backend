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
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Punto;
import com.enrutaglp.backend.models.TipoCamion;

public class TestApp {
	
	
	
	
	public static void main(String[] args) {
		
		//AstarFunciones.testAstarAlgoritmo();
		//FuncionesBackend.testFuncionesBackend();
		
		String path = "D:\\PUCP\\20141929\\20212\\DP1\\my_input\\";
		
		List<Pedido> sales = FuncionesBackend.get_sales(FuncionesBackend.get_folder_content(path+"sales",true,","));
		
		List<Bloqueo> locks = FuncionesBackend.get_locks(FuncionesBackend.get_folder_content(path+"locks",true,","));
		
		List<Mantenimiento> maintenances = FuncionesBackend.get_maintenances(FuncionesBackend.get_file_content(path+"mantenimientos.txt",false,","));
		
		List<TipoCamion> truck_types = FuncionesBackend.get_truck_types( FuncionesBackend.get_file_content(path+"Tipos_camiones.txt",false,","));
		
		List<Camion> trucks = FuncionesBackend.get_trucks(FuncionesBackend.get_file_content(path+"camiones.txt",false,","), truck_types);
		
		
		Punto ini_point = new Punto(0,0);
		Punto final_point = new Punto (sales.get(0).getUbicacionX(), sales.get(0).getUbicacionY());
		AstarFunciones.altoTabla = 11;
		AstarFunciones.anchoTabla = 11;

		List<Punto> intermediates = ini_point.getPuntosIntermedios(final_point, sales.get(0).getFechaPedido(), trucks.get(0),locks);
		AstarFunciones.imprimirCamino(intermediates, locks);
		
    }
}
