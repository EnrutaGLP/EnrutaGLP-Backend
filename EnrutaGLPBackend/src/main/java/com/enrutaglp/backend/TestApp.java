package com.enrutaglp.backend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;

import java.io.FileNotFoundException;


import com.enrutaglp.backend.algorithm.AstarFunciones;
import com.enrutaglp.backend.algorithm.FuncionesBackend;
import com.enrutaglp.backend.algorithm.Genetic;
import com.enrutaglp.backend.algorithm.Individual;
import com.enrutaglp.backend.algorithm.RutaCompleta;
import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Planta;
import com.enrutaglp.backend.models.Punto;
import com.enrutaglp.backend.models.TipoCamion;
import com.enrutaglp.backend.utils.Utils;

public class TestApp {
	
	
	
	
	public static void main(String[] args) {
		
		//AstarFunciones.testAstarAlgoritmo();
		//FuncionesBackend.testFuncionesBackend();
		
		String path = "//home//stevramos//Documents//PUCP//2021-2//DP1//Data//test//Simple_test//";
		
		List<String> file_content = FuncionesBackend.get_folder_content(path+"sales",true,",");
		List<Pedido> sales = FuncionesBackend.get_sales(file_content);
		Map<String, Pedido> map_sales = FuncionesBackend.get_map_sales(sales);
		
		file_content = FuncionesBackend.get_folder_content(path+"locks",true,",");
		List<Bloqueo> locks = FuncionesBackend.get_locks(file_content);
		//List<Bloqueo> locks = new ArrayList<Bloqueo>();
		
		file_content = FuncionesBackend.get_file_content(path+"Tipos_camiones.txt",false,"");
		List<TipoCamion> truck_types = FuncionesBackend.get_truck_types(file_content);
		
		file_content = FuncionesBackend.get_file_content(path+"camiones.txt",false,",");
		List<Camion> trucks = FuncionesBackend.get_trucks(file_content, truck_types);
		Map<String, Camion> map_trucks = FuncionesBackend.get_map_trucks(trucks);
		
		file_content = FuncionesBackend.get_file_content(path+"mantenimientos.txt",false,",");
		List<Mantenimiento> maintenances = FuncionesBackend.get_maintenances(file_content);
		//List<Mantenimiento> maintenances = new ArrayList<Mantenimiento>();
		Map<String, List<Mantenimiento>> map_maintenances = FuncionesBackend.get_map_maintenances(maintenances, trucks);
		
		Punto ini_point = new Punto(1,17);
		Punto final_point = new Punto (sales.get(0).getUbicacionX(), sales.get(0).getUbicacionY());
//		AstarFunciones.altoTabla = 11;
//		AstarFunciones.anchoTabla = 11;
		
		
		List<Punto> intermediates = ini_point.getWayTo(final_point, sales.get(0).getFechaPedido(), trucks.get(0),locks);
		AstarFunciones.imprimirCamino(intermediates, locks);
		List<Planta> plants = new ArrayList<Planta>();
		LocalDateTime horaZero = LocalDateTime.of(2021,11,1,0,0);
		
		//map_sales = Utils.particionarPedidos(map_sales, 5, 5);
		
		Genetic genetic = new Genetic(map_sales, map_trucks, locks, map_maintenances,plants, horaZero);
		int maxIterNoImp = 5;
		int numChildrenToGenerate = 2;
		double wA = 1;
		double wB = 1000;
		double wC = 1000;
		int mu = 10;
		int epsilon = 20;
		double percentageGenesToMutate = 0.3;
		
		
		Individual solution = genetic.run(maxIterNoImp, numChildrenToGenerate, wA, wB, wC, mu, epsilon, percentageGenesToMutate);
		
		Map<String, RutaCompleta>rutasCompletas =  solution.getRutas();
		
		System.out.println();
    }
}
