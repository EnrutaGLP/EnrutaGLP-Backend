package com.enrutaglp.backend.algorithm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Punto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AstarFunciones {
	
	public static int altoTabla = 50;
	public static int anchoTabla = 70;
	
	public static boolean mismaPosicion (Punto p1, Punto p2) {
		int x1 = p1.getUbicacionX();
		int y1 = p1.getUbicacionY();
		int x2 = p2.getUbicacionX();
		int y2 = p2.getUbicacionY();
		return x1 == x2 && y1 == y2; 
	}

	
	public static Punto buscarPunto (Punto pABuscar, List<Punto> lPuntos) {
		
		for (Punto p: lPuntos) {
			if (mismaPosicion(pABuscar, p)) {
				return p;
			}
		}
		return null;
	}
	public static void imprimirCamino (List<Punto>camino, List<Bloqueo> bloqueos) {



		for (int i = 0; i < altoTabla; i ++) {
			for (int j = 0; j < anchoTabla; j ++) {
				Punto pVeif = new Punto(j,i,0);
				if (buscarPunto(pVeif, camino) != null) {
					System.out.print("C");
				}
				else if (hayBloqueosEntre(pVeif, pVeif, bloqueos)){
					System.out.print("B");
				}
				else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	public static boolean esEsquina (Punto p1, Punto p2, Punto p3) {
		int x1 = p1.getUbicacionX();
		int y1 = p1.getUbicacionY();
		int x2 = p2.getUbicacionX();
		int y2 = p2.getUbicacionY();
		int x3 = p3.getUbicacionX();
		int y3 = p3.getUbicacionY();
		
		
		return (x1 != x2 && y2 != y3) || (y1 != y2 && x2 != x3);
	}
	public static List<Punto> obtenerPuntosEsquina(List<Punto> lista) {
		
		List<Punto> lEsquinas = new ArrayList<Punto>();
		for (int i = 1; i < lista.size() - 1; i ++) {
			if (esEsquina(lista.get(i - 1), lista.get(i), lista.get(i + 1))) {
				lEsquinas.add(lista.get(i));
			}
		}
		return lEsquinas;
	}
	public static boolean valorEntre (int valor, int Lim1, int Lim2) {
		return (valor >= Lim1 && valor <= Lim2) || (valor >= Lim2 && valor <= Lim1);
	}


	public static boolean hayBloqueoEntre (Punto p1, Punto p2, Punto pBloq1, Punto pBloq2) {
		
		/* Verificar entre dos puntos hay un bloqueo
		 * 
		 */
		int x1 = p1.getUbicacionX();
		int y1 = p1.getUbicacionY();
		int x2 = p2.getUbicacionX();
		int y2 = p2.getUbicacionY();
		int pBloqX1 = pBloq1.getUbicacionX();
		int pBloqY1 = pBloq1.getUbicacionY();
		int pBloqX2 = pBloq2.getUbicacionX();
		int pBloqY2 = pBloq2.getUbicacionY();
		return (valorEntre(x1, pBloqX1, pBloqX2) && valorEntre(pBloqY1, y1, y2)) ||
				(valorEntre(y1, pBloqY1, pBloqY2) && valorEntre(pBloqX1, x1, x2));
	}
	public static boolean compare_time_lapse (LocalDateTime[] lapse1, LocalDateTime[] lapse2) {
		/* 
		 * Retorna verdadero si lapse1 y lapse2 coinciden en algun espacio de tiempo o instante
		 */
		
		return lapse1[0].compareTo(lapse2[1]) <= 0 && lapse1[1].compareTo(lapse2[0]) >= 0;
	}
	public static List<Bloqueo> filter_locks_by_time_span (List<Bloqueo> locks, LocalDateTime[] my_lapse){

		/*
		 * Filter the locks that match the "my_span".
		 */
		List<Bloqueo> filter = new ArrayList<Bloqueo>();
		for (Bloqueo lock: locks) {
			if (compare_time_lapse(my_lapse, new LocalDateTime[] {lock.getFechaInicio(), lock.getFechaFin()})) {
				filter.add(lock);
			}
		}
		return filter;
	}
	public static boolean hayBloqueosEntre (Punto puntoInicial, Punto puntoFinal, List<Bloqueo> bloqueos) {
		/*
		 * Verifica si hay bloqueo entre 2 puntos, dado un conjunto de bloqueos
		 */
		
		if (bloqueos == null){
			return false;
		}
		for (Bloqueo bloqueo: bloqueos) {
			
			for (int i = 0; i < bloqueo.getPuntos().size(); i ++) {
				
				if (i == 0 && hayBloqueoEntre(puntoInicial, puntoFinal, bloqueo.getPuntos().get(0), bloqueo.getPuntos().get(0)) ||
						(i != 0 && hayBloqueoEntre(puntoInicial, puntoFinal, bloqueo.getPuntos().get(i), bloqueo.getPuntos().get(i - 1)))) {
					return true;
				}
			}
			
		}
		return false;
		
		
	}
	public static int buscarPuntoConMenorF (Punto target_point, List<Punto> my_list) {
		/*
		 * Return the first match
		 */
		
		for (int i = 0; i < my_list.size(); i ++) {
			if (mismaPosicion(my_list.get(i), target_point)) {
				return i;
			}
		}
		return -1;
	}
	public static boolean esPosicionValida(Punto p) {
		int x = p.getUbicacionX();
		int y= p.getUbicacionY();
		return x >= 0 && x < anchoTabla && y >= 0 && y < altoTabla;
	}
	public static void agregarYOrdenar(List<Punto> lista, Punto new_point){
		
		
		int i = 0;
		for (; i < lista.size(); i ++) {
			if (new_point.getAstarF() <= lista.get(i).getAstarF()) {
				break;
			}
		}
		lista.add(i, new_point);
	}
	public static double calcularDistanciasNodos(Punto p1, Punto p2) {
		  double x1 = p1.getUbicacionX(); 
		  double y1 = p1.getUbicacionY(); 
		  double x2 = p2.getUbicacionX(); 
		  double y2 = p2.getUbicacionY();
		  
		  return Math.abs(y2 - y1)+ Math.abs(x2 - x1);
	}
	public static List<Punto> construirCamino (Punto pFin){
		List<Punto> camino = new ArrayList<Punto>();
		Punto antecesor = pFin;
		while (antecesor != null) {
			camino.add(0, antecesor);
			antecesor = antecesor.getAntecesor();
		}
		return camino;
	}
	static private List<Punto> obtenerSucesores (Punto p, List<Bloqueo> bloqueos, Punto exception){
		
		List<Punto> sucesores = new ArrayList<Punto>();
		List<Punto> all = new ArrayList<Punto>();
		int x = p.getUbicacionX();
		int y= p.getUbicacionY();
		all.add(new Punto(x + 1, y, p.getOrden(), p.getCodigoPedido()));
		all.add(new Punto(x - 1, y, p.getOrden(), p.getCodigoPedido()));
		all.add(new Punto(x, y + 1, p.getOrden(), p.getCodigoPedido()));
		all.add(new Punto(x, y - 1, p.getOrden(), p.getCodigoPedido()));
		for (Punto p: all) {
			boolean is_exception_or_not_lock = mismaPosicion(p, exception) || !hayBloqueosEntre(p, p, bloqueos);
			if (esPosicionValida(p) && is_exception_or_not_lock) {
				sucesores.add(p);
			}
		}
		return sucesores;
	}
	static public List<Punto> astarAlgoritmo(Punto ini_point, Punto target_point, List<Bloqueo> locks) {
		/*
		 * Return the path from ini_point to target_point, given a set of locks
		 * The path consists of a set of corner points.
		 * Source: https://ieeexplore-ieee-org.ezproxybib.pucp.edu.pe/stamp/stamp.jsp?tp=&arnumber=8996720
		 * 
		 */
		List<Punto> open = new ArrayList<Punto>();
		List<Punto> closed = new ArrayList<Punto>();
		ini_point.setAstarF(calcularDistanciasNodos(ini_point, target_point));
		ini_point.setAntecesor(null);
		open.add(ini_point);
		
		while (!open.isEmpty()) {
			
			
			Punto record = open.remove(0);
			agregarYOrdenar(closed, record);
			
			List<Punto> succs = obtenerSucesores(record, locks, target_point);
			
			for (Punto succ: succs) {
				succ.setAntecesor(record);
				if (mismaPosicion(succ,target_point)) {
					return obtenerPuntosEsquina(construirCamino(succ));
				}
				
				succ.setAstarG(record.getAstarG() + calcularDistanciasNodos(succ, record));
				succ.setAstarH(calcularDistanciasNodos(succ, target_point));
				succ.setAstarF(succ.getAstarG() + succ.getAstarH());
				
				int i_match_open_point = buscarPuntoConMenorF(succ, open);
				int i_match_closed_point = buscarPuntoConMenorF(succ, closed);
				boolean succ_is_not_in_open_and_close = i_match_open_point < 0 && i_match_closed_point < 0;
				boolean better_than_match_closed_point = i_match_closed_point >= 0 &&
						closed.get(i_match_closed_point).getAstarG() > succ.getAstarG();
				
				if (succ_is_not_in_open_and_close) {
					
					agregarYOrdenar (open, succ);
				}
				else if (better_than_match_closed_point){
					closed.get(i_match_closed_point).setAstarG(succ.getAstarG());
					closed.get(i_match_closed_point).setAstarH(succ.getAstarH());
					closed.get(i_match_closed_point).setAstarF(succ.getAstarF());
					closed.get(i_match_closed_point).setAntecesor(record);
				}
			}
		}

		return null;
	}
	public static List<Bloqueo> generarListaBloqueos(){
		
		List<Bloqueo> bloqueos = new ArrayList<Bloqueo>();
		List<Punto> pEsqBloq = new ArrayList<Punto>();
		
		
		pEsqBloq.add(new Punto (1,0,44));
		pEsqBloq.add(new Punto (1,2,44));
		Bloqueo bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		
		pEsqBloq = new ArrayList<Punto>();
		pEsqBloq.add(new Punto (0,4,44));
		pEsqBloq.add(new Punto (9,4,44));
		bloqueo = new Bloqueo (1,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		bloqueo.setPuntos(pEsqBloq);
		bloqueos.add(bloqueo);
		return bloqueos;
	}
	
	public static void testAstarAlgoritmo () {
		
		Punto puntoIni = new Punto(0,0,4);
		Punto puntoFinal = new Punto(2,6,2);
		
		List<Bloqueo> bloqueos = generarListaBloqueos();
		LocalDateTime fecha = LocalDateTime.now();
		Camion camion = new Camion("CODIGO",3,4,10.5,5.6);
		
		List<Punto> puntosIntermedios = puntoIni.pruebaAStar(puntoFinal, bloqueos);
		

		imprimirCamino(puntosIntermedios, bloqueos);
	}

	public static boolean hayBloqueosEnMiCamino (List<Punto> points, List<Bloqueo> locks) {
		
		for (int i = 1; i < points.size(); i ++) {
			if (AstarFunciones.hayBloqueosEntre(points.get(i - 1), points.get(i), locks)) {
				return true;
			}
		}
		return false;
	}
}
