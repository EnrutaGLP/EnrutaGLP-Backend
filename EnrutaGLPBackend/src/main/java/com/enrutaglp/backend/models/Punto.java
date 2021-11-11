package com.enrutaglp.backend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

	public Punto(int id, int ubicacionX, int ubicacionY, int orden, int idBloqueo) {
		this.id = id;
		this.ubicacionX = ubicacionX;
		this.ubicacionY = ubicacionY;
		this.orden = orden;
		this.idBloqueo = idBloqueo;
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
	private Punto getPuntoMenorF (Punto puntoABuscar, List<Punto> lista) {
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
	
	private List<Punto> obtenerSucesores (Punto p){
		
		List<Punto> sucesores = new ArrayList<Punto>();
		sucesores.add(new Punto(p.getUbicacionX() + 1, p.getUbicacionY(), p.getOrden()));
		sucesores.add(new Punto(p.getUbicacionX() - 1, p.getUbicacionY(), p.getOrden()));
		sucesores.add(new Punto(p.getUbicacionX(), p.getUbicacionY() + 1, p.getOrden()));
		sucesores.add(new Punto(p.getUbicacionX(), p.getUbicacionY() - 1, p.getOrden()));
		return sucesores;
	}
	
	private List<Punto> astarAlgoritmo(Punto puntoIni, Punto puntoFin, List<Bloqueo> bloqueos, LocalDateTime fechaIni, Camion camion) {
		
		List<Punto> listaA = new ArrayList<Punto>();
		List<Punto> listaB = new ArrayList<Punto>();
		puntoIni.setAstarF(0);
		listaA.add(puntoIni);
		
		while (!listaA.isEmpty()) {
			Punto q = listaA.remove(0);//Se asume que listaA esta ordenada de menor a mayor según el atributo astarF de cada punto
			for (Punto sucesor: obtenerSucesores(q)) {
				
				if (mismaPosicion(sucesor,q)) {
					return null;
				}
				sucesor.setAstarG(q.getAstarG() + calcularDistanciasNodos(sucesor, q));
				sucesor.setAstarH(calcularDistanciasNodos(sucesor, puntoFin));
				sucesor.setAstarF(sucesor.getAstarG() + sucesor.getAstarH());
				
				Punto puntoAuxA = getPuntoMenorF(sucesor, listaA);
				Punto puntoAuxB = getPuntoMenorF(sucesor, listaB);
				
				if (puntoAuxA != null && puntoAuxA.getAstarF() < sucesor.getAstarF()) {
					continue;
				}
				if (puntoAuxB != null && puntoAuxB.getAstarF() < sucesor.getAstarF()) {
					continue;
				}
				agregarYOrdenar (listaA, sucesor);
			}
			listaB.add(q);
		}
		return null;
	}
	
	
	private boolean hayBloqueosEntre (Punto puntoInicial, Punto puntoFinal, List<Bloqueo> bloqueos, LocalDateTime fechaIni, Camion camion) {
		
		List<Punto> parBloqueados = new ArrayList<Punto>(); //Par de puntos que forman un bloqueo
		
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
	
}
