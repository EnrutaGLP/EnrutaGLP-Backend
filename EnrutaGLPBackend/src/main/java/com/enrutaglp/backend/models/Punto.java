package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class Punto {

	@Setter
	private int id;
	private int ubicacionX;
	private int ubicacionY;
	@Setter
	private int orden; 
	@Setter
	private int idBloqueo;
	@Setter
	private String codigoPedido;
	@Setter
	private boolean planta;
	@Setter
	private double astarF;
	@Setter
	private double astarG;
	@Setter
	private double astarH;
	
	public Punto() {
		codigoPedido = null;
	}
	
	public Punto(int id, int ubicacionX, int ubicacionY, int orden, int idBloqueo) {
		this.id = id;
		this.ubicacionX = ubicacionX;
		this.ubicacionY = ubicacionY;
		this.orden = orden;
		this.idBloqueo = idBloqueo;
	}
	
	
	
	public Punto clone() {
		Punto p = new Punto(); 
		p.ubicacionX = this.ubicacionX; 
		p.ubicacionY = this.ubicacionY; 
		p.orden = this.orden; 
		p.planta = this.planta; 
		p.codigoPedido = this.codigoPedido;
		return p; 
	}
	
	public Punto(int ubicacionX, int ubicacionY, int orden) {
		this.ubicacionX = ubicacionX;
		this.ubicacionY = ubicacionY;
		this.orden = orden;
		
		if((this.ubicacionX==12)&(this.ubicacionY==8)) {
			this.setPlanta(true);
		}
		else {
			this.setPlanta(false);
		}
		this.codigoPedido = null;
	}
	
	public Punto(int ubicacionX, int ubicacionY, int orden, String codigoPedido) {
		this.ubicacionX = ubicacionX;
		this.ubicacionY = ubicacionY;
		this.orden = orden;
		
		if((this.ubicacionX==12)&(this.ubicacionY==8)) {
			this.setPlanta(true);
		}
		else {
			this.setPlanta(false);
			this.codigoPedido = codigoPedido; 
		}
	}
	
	public void setUbicacionX(int ubicacionX) {
		this.ubicacionX = ubicacionX;
		
		if((this.ubicacionX==12)&(this.ubicacionY==8)) {
			this.setPlanta(true);
		}
		else {
			this.setPlanta(false);
		}
		
	}
	
	public void setUbicacionY(int ubicacionY) {
		this.ubicacionY = ubicacionY;
		
		if((this.ubicacionX==12)&(this.ubicacionY==8)) {
			this.setPlanta(true);
		}
		else {
			this.setPlanta(false);
		}
		
	}


	public double calcularDistanciasNodos(Punto puntoNodo) {
		  double x1 = this.getUbicacionX(); 
		  double y1 = this.getUbicacionY(); 
		  double x2 = puntoNodo.getUbicacionX(); 
		  double y2 = puntoNodo.getUbicacionY();
		  
		  return Math.abs(y2 - y1)+ Math.abs(x2 - x1);
	}
	private double calcularDistanciasNodos(Punto p1, Punto p2) {
		  double x1 = p1.getUbicacionX(); 
		  double y1 = p1.getUbicacionY(); 
		  double x2 = p2.getUbicacionX(); 
		  double y2 = p2.getUbicacionY();
		  
		  return Math.abs(y2 - y1)+ Math.abs(x2 - x1);
	}
	private boolean esLimitadoPor (int valor, int Lim1, int Lim2) {
		return (valor >= Lim1 && valor <= Lim2) || (valor >= Lim2 && valor <= Lim1);
	}
	private boolean mismaPosicion (Punto p1, Punto p2) {
		int x1 = p1.getUbicacionX();
		int y1 = p1.getUbicacionY();
		int x2 = p2.getUbicacionX();
		int y2 = p2.getUbicacionY();
		return x1 == x2 && y1 == y2; 
	}
	private Punto buscarPuntoConMenorF (Punto puntoABuscar, List<Punto> lista) {
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
	
	private List<Punto> agregarYOrdenar(List<Punto> lista, Punto puntoNuevo){
		
		
		int i;
		for (i = 0; i < lista.size(); i ++) {
			if (puntoNuevo.getAstarF() <= lista.get(i).getAstarF()) {
				break;
			}
		}
		lista.add(i, puntoNuevo);
		return lista;
	}
	
	private boolean estaBloqueado (Punto pVerif, List<Bloqueo> bloqueos) {
		
		
		for (Bloqueo bloqueo: bloqueos) {
			for (Punto pBloqueado: bloqueo.getPuntos()) {
				if (mismaPosicion(pVerif, pBloqueado)) {
					return true;
				}
			}
		}
		return false;
	}
	private boolean esPosicionValida(Punto p) {
		int x = p.getUbicacionX();
		int y= p.getUbicacionY();
		return x >= 0 && y >= 0;
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
			if (esPosicionValida(pAux) && !estaBloqueado(pAux, bloqueos)) {
				sucesores.add(pAux);
			}
		}
		return sucesores;
	}
	
	private boolean esEsquina (Punto p1, Punto p2, Punto p3) {
		int x1 = p1.getUbicacionX();
		int y1 = p1.getUbicacionY();
		int x2 = p2.getUbicacionX();
		int y2 = p2.getUbicacionY();
		int x3 = p3.getUbicacionX();
		int y3 = p3.getUbicacionY();
		
		
		return (x1 != x2 && y2 != y3) || (y1 != y2 && x2 != x3);
	}
	private List<Punto> obtenerPuntosEsquina(List<Punto> lista) {
		
		List<Punto> lEsquinas = new ArrayList<Punto>();
		for (int i = 1; i < lista.size() - 1; i ++) {
			if (esEsquina(lista.get(i - 1), lista.get(i), lista.get(i + 1))) {
				lEsquinas.add(lista.get(i));
			}
		}
		return lEsquinas;
	}
	
	private List<Punto> astarAlgoritmo(Punto puntoIni, Punto puntoFin, List<Bloqueo> bloqueos, LocalDateTime fechaIni, Camion camion) {
		
		List<Punto> lAbierta = new ArrayList<Punto>();
		List<Punto> lCerrada = new ArrayList<Punto>();
		puntoIni.setAstarF(0);
		lAbierta.add(puntoIni);
		
		while (!lAbierta.isEmpty()) {
			Punto Q = lAbierta.remove(0);//Se asume que lAbierta esta ordenada de menor a mayor según el atributo astarF de cada punto
			for (Punto sucesor: obtenerSucesores(Q, bloqueos)) {
				
				if (mismaPosicion(sucesor,Q)) {
					return null;
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
		lCerrada = obtenerPuntosEsquina(lCerrada);
		lCerrada.remove(0);
		return lCerrada;
	}
	
	
	private boolean hayBloqueosEntre (Punto puntoInicial, Punto puntoFinal, List<Bloqueo> bloqueos, LocalDateTime fechaIni, Camion camion) {
		
		
		for (Bloqueo bloqueo: bloqueos) {
			for (int i = 0; i < bloqueo.getPuntos().size() - 1; i ++) {
				//Recorrer cada punto del bloqueo hasta el penúltimo con el fin de formar pares de puntos bloqueados
				// en cada ciclo.
				// Por ejemplo: Para los puntos boqueados (p1, p2, p3), se tendría (p1, p2) y (p2, p3)
				
				int puntoBloqIniX = bloqueo.getPuntos().get(i).getUbicacionX();
				int puntoBloqFinX = bloqueo.getPuntos().get(i + 1).getUbicacionX();
				int puntoIniX = puntoInicial.getUbicacionX();
				int puntoFinX = puntoFinal.getUbicacionX();
				int puntoBloqIniY = bloqueo.getPuntos().get(i).getUbicacionY();
				int puntoBloqFinY = bloqueo.getPuntos().get(i + 1).getUbicacionY();
				int puntoIniY = puntoInicial.getUbicacionY();
				int puntoFinY = puntoFinal.getUbicacionY();
				
				if (((puntoBloqIniX == puntoBloqFinX && esLimitadoPor(puntoBloqIniX, puntoIniX, puntoFinX)) ||
						(puntoIniX == puntoFinX && esLimitadoPor(puntoIniX, puntoBloqIniX, puntoBloqFinX))) &&
						((puntoBloqIniY == puntoBloqFinY && esLimitadoPor(puntoBloqIniY, puntoIniY, puntoFinY)) ||
								(puntoIniY == puntoFinY && esLimitadoPor(puntoIniY, puntoBloqIniY, puntoBloqFinY)))) {
					return true;
				}
			}
		}
		return false;
	}
	public List<Punto> getPuntosIntermedios(Punto puntoFinal, List<Bloqueo> bloqueos, LocalDateTime fechaIni, Camion camion) {
		/*
		 * Punto intermedio: punto esquina
		 * 
		 */
		List<Punto> puntosIntemedios = new ArrayList<Punto>();

		
		/*
		this.distanciaRecorrida = this.puntos.get(this.puntos.size()-2).calcularDistanciasNodos(this.puntos.get(this.puntos.size()-1));
		int tiempo = (int) (this.distanciaRecorrida/this.camion.getTipo().getVelocidadPromedio());
		fechafinal = fechaIni.plusHours(tiempo);
		*/
		//verificar fechaini y fechafin, si no hay bloqueos devolver listapuntos
		
		//si hay bloqueos A *
		Punto puntoEsquina = new Punto(this.getUbicacionX(), puntoFinal.getUbicacionY(), this.getOrden());
		if (!hayBloqueosEntre (this, puntoEsquina, bloqueos, fechaIni, camion) &&
				!hayBloqueosEntre (puntoEsquina, puntoFinal, bloqueos, fechaIni, camion)) {
			puntosIntemedios.add(puntoEsquina);
			return puntosIntemedios;
		}
		puntoEsquina = new Punto(puntoFinal.getUbicacionX(), this.getUbicacionY(), this.getOrden());
		if (!hayBloqueosEntre (this, puntoEsquina, bloqueos, fechaIni, camion) &&
				!hayBloqueosEntre (puntoEsquina, puntoFinal, bloqueos, fechaIni, camion)) {
			puntosIntemedios.add(puntoEsquina);
			return puntosIntemedios;
		}
		

		return astarAlgoritmo(this, puntoFinal, bloqueos, fechaIni, camion);

	}
	
	public List<Punto> getPuntosIntermedios(Punto puntoFinal, LocalDateTime fechaIni, Camion camion){
		List<Punto> puntosIntemedios = new ArrayList<Punto>();
		
		int sentido = ThreadLocalRandom.current().nextInt(0, 2);
		
		if(sentido==0) {
			//eje x primero
			
			Punto nuevoPunto = new Punto(this.ubicacionX, puntoFinal.getUbicacionY(), this.orden + 1, this.codigoPedido);
			puntosIntemedios.add(nuevoPunto);
		}
		else {
			//eje y primero
			Punto nuevoPunto = new Punto(puntoFinal.getUbicacionX(), this.ubicacionY, this.orden + 1, this.codigoPedido);
			puntosIntemedios.add(nuevoPunto);
		}
		
		return puntosIntemedios;
	}

	
}
