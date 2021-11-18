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
	
	public static int altoTabla = 11;
	public static int anchoTabla = 11;
	
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
	public static boolean hayBloqueosEntre (Punto puntoInicial, Punto puntoFinal, List<Bloqueo> bloqueos) {
		
		if (bloqueos == null){
			return false;
		}
		for (Bloqueo bloqueo: bloqueos) {
			for (int i = 0; i < bloqueo.getPuntos().size(); i ++) {
				/* 
				 * Si el punto bloqueado no es el primero entonces es un punto esquina
				 * Por ejemplo, llamémosle p(i)
				 * Devolver si hay bloqueo entre
				 * puntoInicial, puntoFinal, p(i) y p(i - 1),
				 */
				if (i == 0 && hayBloqueoEntre(puntoInicial, puntoFinal, bloqueo.getPuntos().get(0), bloqueo.getPuntos().get(0)) ||
						(i != 0 && hayBloqueoEntre(puntoInicial, puntoFinal, bloqueo.getPuntos().get(i), bloqueo.getPuntos().get(i - 1)))) {
					return true;
				}
			}
			
		}
		return false;
		
		
	}
	public static Punto buscarPuntoConMenorF (Punto puntoABuscar, List<Punto> lista) {
		/*
		 * Se asume que lista esta ordenada de menor a mayor según el atributo astarF de cada punto
		 */
		
		for (Punto punto: lista) {
			if (mismaPosicion(punto, puntoABuscar)) {
				return punto;
			}
		}
		return null;
	}
	public static boolean esPosicionValida(Punto p) {
		int x = p.getUbicacionX();
		int y= p.getUbicacionY();
		return x >= 0 && x < anchoTabla && y >= 0 && y < altoTabla;
	}
	public static List<Punto> agregarYOrdenar(List<Punto> lista, Punto puntoNuevo){
		
		
		int i;
		for (i = 0; i < lista.size(); i ++) {
			if (puntoNuevo.getAstarF() <= lista.get(i).getAstarF()) {
				break;
			}
		}
		lista.add(i, puntoNuevo);
		return lista;
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
	private List<Punto> obtenerSucesores (Punto p, List<Bloqueo> bloqueos){
		
		List<Punto> sucesores = new ArrayList<Punto>();
		List<Punto> lAux = new ArrayList<Punto>();
		int x = p.getUbicacionX();
		int y= p.getUbicacionY();
		lAux.add(new Punto(x + 1, y, p.getOrden()));
		lAux.add(new Punto(x - 1, y, p.getOrden()));
		lAux.add(new Punto(x, y + 1, p.getOrden()));
		lAux.add(new Punto(x, y - 1, p.getOrden()));
		
		for (Punto pAux: lAux) {
			if (esPosicionValida(pAux) && !hayBloqueosEntre(pAux, pAux, bloqueos)) {
				sucesores.add(pAux);
			}
		}
		return sucesores;
	}
	public List<Punto> astarAlgoritmo(Punto puntoIni, Punto puntoFin, List<Bloqueo> bloqueos) {
		
		List<Punto> lAbierta = new ArrayList<Punto>();
		List<Punto> lCerrada = new ArrayList<Punto>();
		puntoIni.setAstarF(0);
		puntoIni.setAntecesor(null);
		lAbierta.add(puntoIni);
		
		while (!lAbierta.isEmpty()) {
			Punto Q = lAbierta.remove(0);//Se asume que lAbierta esta ordenada de menor a mayor según el atributo astarF de cada punto
			for (Punto sucesor: obtenerSucesores(Q, bloqueos)) {
				sucesor.setAntecesor(Q);
				if (mismaPosicion(sucesor,puntoFin)) {
					
					return obtenerPuntosEsquina(construirCamino(sucesor));
				}
				
				sucesor.setAstarG(Q.getAstarG() + calcularDistanciasNodos(sucesor, Q)); //G(S) = G(Q) + DISTANCIA(Q, S)
				sucesor.setAstarH(calcularDistanciasNodos(sucesor, puntoFin)); //H(S) = DISTANCIA (S, Z)
				sucesor.setAstarF(sucesor.getAstarG() + sucesor.getAstarH());
				
				Punto pMenorFenA = buscarPuntoConMenorF(sucesor, lAbierta);
				Punto pMenorFenC = buscarPuntoConMenorF(sucesor, lCerrada);
				
				if (pMenorFenA != null && pMenorFenA.getAstarF() < sucesor.getAstarF()) {
					continue;
				}
				if (pMenorFenC != null && pMenorFenC.getAstarF() < sucesor.getAstarF()) {
					continue;
				}
				lAbierta = agregarYOrdenar (lAbierta, sucesor);
			}
			lCerrada.add(Q);
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
}
