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
import com.enrutaglp.backend.models.Ruta;
import com.enrutaglp.backend.models.TipoCamion;
import com.enrutaglp.backend.utils.Utils;

public class TestApp {
	
	
	
	
	public static void main(String[] args) {
		
		//AstarFunciones.testAstarAlgoritmo();
		//FuncionesBackend.testFuncionesBackend();
		
		//String path = "//home//stevramos//Documents//PUCP//2021-2//DP1//Data//test//Simple_test//";
		
		//path 20
		String path = "//home//stevramos//Documents//PUCP//2021-2//DP1//Data//test//Plan//out50//";
//		String path = "D:\\PUCP\\20141929\\20212\\DP1\\my_input\\";
		//List<String> file_content = FuncionesBackend.get_folder_content(path+"sales",true,",");
		List<String> file_content = FuncionesBackend.get_folder_content(path+"3",true,",");
		List<Pedido> sales = FuncionesBackend.get_sales(file_content);
		Map<String, Pedido> map_sales = FuncionesBackend.get_map_sales(sales);
		
		file_content = FuncionesBackend.get_folder_content(path+"locks",true,",");
		//List<Bloqueo> locks = FuncionesBackend.get_locks(file_content);
		List<Bloqueo> locks = new ArrayList<Bloqueo>();
		
		file_content = FuncionesBackend.get_file_content(path+"Tipos_camiones.txt",false,"");
		List<TipoCamion> truck_types = FuncionesBackend.get_truck_types(file_content);
		
		file_content = FuncionesBackend.get_file_content(path+"camiones.txt",false,",");
		List<Camion> trucks = FuncionesBackend.get_trucks(file_content, truck_types);
		Map<String, Camion> map_trucks = FuncionesBackend.get_map_trucks(trucks);
		
		file_content = FuncionesBackend.get_file_content(path+"mantenimientos.txt",false,",");
		//List<Mantenimiento> maintenances = FuncionesBackend.get_maintenances(file_content);
		List<Mantenimiento> maintenances = new ArrayList<Mantenimiento>();
		Map<String, List<Mantenimiento>> map_maintenances = FuncionesBackend.get_map_maintenances(maintenances, trucks);
		
		Punto ini_point = new Punto(1,17);
		Punto final_point = new Punto (sales.get(0).getUbicacionX(), sales.get(0).getUbicacionY());
		
		
		List<Punto> intermediates = ini_point.getWayTo(final_point, sales.get(0).getFechaPedido(), trucks.get(0),locks);
//		AstarFunciones.imprimirCamino(intermediates, locks);
		
		//map_sales = Utils.particionarPedidos(map_sales, 5, 5);
		List<Planta> plants = new ArrayList<Planta>();
		LocalDateTime horaZero = LocalDateTime.of(2021,11,1,0,0);
		
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
		
		int orden = 0;
		String output = "";	
		
		System.out.println("Solucion algoritmo " + map_sales.size()  + " pedidos");
		System.out.println("No se entregaron " + solution.getCantidadPedidosNoEntregados() + " pedidos");
		
		
		for(RutaCompleta rc : rutasCompletas.values()) {
			//if(rc.getRutas() != null && rc.getRutas().size()>0) {
				System.out.println("Camion: " + rc.getCamion().getCodigo());
				System.out.println("Fecha transcurrida: " + rc.getFechaHoraTranscurrida());
				System.out.println("Pedidos entregados por el camion: " + rc.getPedidos().size());
				System.out.println("Pedidos no entreg: " + rc.getCantPedidosNoEntregados());
				System.out.println("GLP no entregados: " + rc.getGlpNoEntregado());
				System.out.println("Petroleo consumido: " + rc.getPetroleoConsumido());
				System.out.println("Costo ruta: " + rc.getCostoRuta());
				System.out.println("Distancia recorrida: " + rc.getDistanciaRecorrida());
				
				List<Ruta> rutas = rc.getRutas();
				orden = 0;
				
				for(Ruta r: rutas){
					orden = orden + 1;
					output = "";
					System.out.println("Ruta num "+ orden);
					for(int j=0;j<r.getPuntos().size();j++) {
						output = output + "(" + r.getPuntos().get(j).getUbicacionX() 
								+ ", " +  r.getPuntos().get(j).getUbicacionY() + ") ";
					}
					System.out.println(output);
				}
				System.out.println();
			//}
		}
		
		
    }
}
