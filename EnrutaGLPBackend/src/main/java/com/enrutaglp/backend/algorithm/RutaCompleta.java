package com.enrutaglp.backend.algorithm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.EntregaPedido;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Punto;
import com.enrutaglp.backend.models.Recarga;
import com.enrutaglp.backend.models.Ruta;
import com.enrutaglp.backend.utils.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RutaCompleta implements Comparable<RutaCompleta> {
	
	private Map<String, Pedido> pedidos;
	private List<Punto> nodos;
	private List<Ruta> rutas;
	private List<Mantenimiento> mantenimientos;
	private List<Bloqueo> bloqueos;
	@Setter
	private Camion camion;
	@Setter
	private LocalDateTime fechaHoraTranscurrida; //inicializar con la fechahoraactual
	@Setter
	private int cantPedidosNoEntregados;
	@Setter
	private double glpNoEntregado;
	@Setter
	private double petroleoConsumido;
	@Setter
	private double costoRuta;
	@Setter
	private double distanciaRecorrida;
	
	public RutaCompleta(Camion camion, LocalDateTime fechaHoraTranscurrida, List<Mantenimiento> mantenimientos, List<Bloqueo> bloqueos) {
		this.pedidos = new HashMap<String, Pedido>();
		this.camion = new Camion(camion);
		this.fechaHoraTranscurrida = fechaHoraTranscurrida;
		this.cantPedidosNoEntregados = 0;
		this.glpNoEntregado = 0;
		this.petroleoConsumido = 0;
		this.costoRuta = 0;
		this.distanciaRecorrida = 0;
		
		this.nodos = new ArrayList<Punto>();
		this.costoRuta = 0;
		
		Punto planta = new Punto(12, 8, 0);
		this.nodos.add(planta);
		
		this.rutas = new ArrayList<Ruta>();
		this.mantenimientos = mantenimientos;
		this.bloqueos = bloqueos;
		
	}

	public double calcularCostoRuta(Map<String, Pedido> pedidosOriginales,double wa, double wb, double wc) {
		this.cantPedidosNoEntregados = 0;
		this.glpNoEntregado = 0;
		this.costoRuta = wa*this.getPetroleoConsumido(); 
		
		for(String key: pedidosOriginales.keySet()) {
				
				if(!this.pedidos.containsKey(key)) {
					this.glpNoEntregado += pedidosOriginales.get(key).getCantidadGlp(); 
					this.cantPedidosNoEntregados += 1;
					//solo en caso el tiempo limite sea menor al tiempo actual
				}
			}
		
		assert this.cantPedidosNoEntregados + this.pedidos.size() == pedidosOriginales.size() : "La suma de pedidos y no entregados no esta bien";
			
			this.costoRuta += wb*this.glpNoEntregado;
		
		
		return this.costoRuta;
	}

	public void copiarRuta(RutaCompleta ruta) {
		this.camion = new Camion(ruta.getCamion());
		this.fechaHoraTranscurrida = ruta.getFechaHoraTranscurrida();
		this.setPedidos(ruta.getPedidos());
		this.setNodos(ruta.getNodos());
		this.setRutas(ruta.getRutas());
		
		this.mantenimientos = ruta.getMantenimientos();
		this.bloqueos = ruta.getBloqueos();
		
		this.costoRuta = ruta.getCostoRuta();		
		this.cantPedidosNoEntregados = ruta.getCantPedidosNoEntregados();
		this.glpNoEntregado = ruta.getGlpNoEntregado();
		this.petroleoConsumido = ruta.getPetroleoConsumido();
		this.distanciaRecorrida = ruta.getDistanciaRecorrida();
	}

	public void setNodos(List<Punto> nodos) {
		this.nodos = new ArrayList<Punto>();
		Iterator<Punto> iterator = nodos.iterator();
		 
		while(iterator.hasNext())
		{
			this.nodos.add(((Punto)iterator.next()).clone());  
		}
	}
	
	public void setRutas(List<Ruta> rutas) {
		this.rutas = new ArrayList<Ruta>();
		Iterator<Ruta> iterator = rutas.iterator();
		 
		while(iterator.hasNext())
		{
			this.rutas.add(((Ruta)iterator.next()).clone());  
		}
	}

	public void setPedidos(Map<String, Pedido> pedidos) {
		Map<String,Pedido> copia = new HashMap<String, Pedido>(); 
		for(String key: pedidos.keySet()) {
			copia.put(key,new Pedido(pedidos.get(key)));
		}
		this.pedidos = copia;
	}

	public void insertarPedido(Pedido pedido, List<Punto> puntosTotales) {
		pedidos.put(pedido.getCodigo(), new Pedido(pedido));
		Punto punto = new Punto(pedido.getUbicacionX(),
				pedido.getUbicacionY(), this.nodos.size(),pedido.getCodigo());
		this.nodos.add(punto);		
		
		
		Ruta rutaNueva = new EntregaPedido(puntosTotales, 
								this.camion, this.fechaHoraTranscurrida, this.rutas.size(), 
								pedido.getCantidadGlp(), pedido);
		double distanciaPuntos = rutaNueva.getDistanciaRecorrida();
		
		double consumoPetroleo = this.camion.calcularConsumoPetroleo(distanciaPuntos);
		rutaNueva.setConsumoPetroleo(consumoPetroleo);
		
		this.camion.setCargaActualPetroleo(this.camion.getCargaActualPetroleo()-consumoPetroleo);
		this.camion.setCargaActualGLP(this.camion.getCargaActualGLP() - pedido.getCantidadGlp());
		rutaNueva.setCamion(this.camion);
		
		this.petroleoConsumido += consumoPetroleo;
		this.distanciaRecorrida += distanciaPuntos;
		this.fechaHoraTranscurrida = rutaNueva.getHoraLlegada().plusMinutes(10);
		
		this.rutas.add(rutaNueva);
	}
	

	public void insertarPuntoPlanta() {
		
		if(!this.nodos.get(this.nodos.size()-1).isPlanta()) {
			Punto punto = new Punto(12,8, this.nodos.size());
			this.nodos.add(punto);
			
			List<Punto> puntosTotales = this.nodos.get(this.nodos.size()-2).getPuntosIntermedios(punto, this.fechaHoraTranscurrida, this.camion, this.bloqueos);
			puntosTotales.add(0, this.nodos.get(this.nodos.size()-2));
			puntosTotales.add(punto);
			
			
			Ruta rutaNueva = new Recarga(puntosTotales, 
					this.camion, this.fechaHoraTranscurrida, this.rutas.size(),
					this.camion.getTipo().getCapacidadGLP()-this.camion.getCargaActualGLP());
			double distanciaPuntos = rutaNueva.getDistanciaRecorrida();
			
			double consumoPetroleo = this.camion.calcularConsumoPetroleo(distanciaPuntos);
			rutaNueva.setConsumoPetroleo(consumoPetroleo);

			this.camion.setCargaActualGLP(this.camion.getTipo().getCapacidadGLP());
			this.camion.setCargaActualPetroleo(this.camion.getTipo().getCapacidadTanque());
			rutaNueva.setCamion(this.camion);
			
			this.petroleoConsumido += consumoPetroleo;
			this.distanciaRecorrida += distanciaPuntos;
			this.fechaHoraTranscurrida = rutaNueva.getHoraLlegada();
			
			this.rutas.add(rutaNueva);
		}
		
	}

	
	public List<Punto> esFactible(Pedido pedido) {
		//boolean factible;
		int valorMinRecPI = 5;
		List<Punto> puntosTotales = new ArrayList<Punto>();
		//el pedido debio haberse hech o antes de la hora actual y la carga actual de glp debe ser mayor o igual a la del pedido
		if((this.fechaHoraTranscurrida.isBefore(pedido.getFechaPedido()) ||
				pedido.getFechaLimite().isBefore(this.fechaHoraTranscurrida)) ||
				(pedido.getCantidadGlp() - this.camion.getCargaActualGLP() > valorMinRecPI) ||
				(pedido.getCantidadGlp() > this.camion.getTipo().getCapacidadGLP())) {
			//vacio
			return puntosTotales;
		}
		

		//el camion debe tener combustible para ir al pedido y regresar a la planta
		//el camion debe tener GLP para el pedido
		//el camion debe entregar antes de la hora maxima
		Punto punto = new Punto(pedido.getUbicacionX(),
				pedido.getUbicacionY(), this.nodos.size(),pedido.getCodigo());
		
		Punto planta = new Punto(12, 8, 5000);

		
		//planta intermedia inicio
		List<Punto> puntosIntemediosAPI = new ArrayList<Punto>();
		if((pedido.getCantidadGlp()>this.camion.getCargaActualGLP()) &&
				(pedido.getCantidadGlp() - this.camion.getCargaActualGLP() <= valorMinRecPI) &&
				(this.camion.getTipo().getCapacidadGLP()>=pedido.getCantidadGlp())) {
			//ir a planta intermedia a recargar
			Punto planteIntN = new Punto(42, 42, 5001);
			Punto planteIntE = new Punto(63, 3, 5002);			
			
			double distanciaPuntoAPIN = Utils.calcularDistanciaTodosPuntos(new ArrayList<Punto>(Arrays.asList(this.nodos.get(this.nodos.size()-1), planteIntN)));
			double distanciaPuntoAPIE = Utils.calcularDistanciaTodosPuntos(new ArrayList<Punto>(Arrays.asList(this.nodos.get(this.nodos.size()-1), planteIntE)));
			double distanciaPuntoAPP = Utils.calcularDistanciaTodosPuntos(new ArrayList<Punto>(Arrays.asList(this.nodos.get(this.nodos.size()-1), planta)));
			
			double distaMin = distanciaPuntoAPIN > distanciaPuntoAPIE ? distanciaPuntoAPIE : distanciaPuntoAPIN;
			Punto plantaAIr = distanciaPuntoAPIN > distanciaPuntoAPIE ? planteIntE : planteIntN;
			plantaAIr = distaMin > distanciaPuntoAPP ? planta : plantaAIr;
			
			if(!plantaAIr.isPlanta()) {
				puntosIntemediosAPI = this.nodos.get(this.nodos.size()-1).getPuntosIntermedios(plantaAIr, this.fechaHoraTranscurrida, this.camion, this.bloqueos);
				puntosIntemediosAPI.add(0, this.nodos.get(this.nodos.size()-1));
				puntosIntemediosAPI.add(plantaAIr);				
			}
			else {
				return puntosTotales;
			}

		}
		//planta intermedia fin
		
		
		double cargaActualGLPOrig = this.camion.getCargaActualGLP();
		double cargaActualPetOrig = this.camion.getCargaActualPetroleo();
		List<Punto> puntosIntemediosAB = new ArrayList<Punto>();
		//flujo normal
		if(puntosIntemediosAPI.size()==0) {
			puntosIntemediosAB = this.nodos.get(this.nodos.size()-1).getPuntosIntermedios(punto, this.fechaHoraTranscurrida, this.camion, this.bloqueos);
			puntosIntemediosAB.add(0, this.nodos.get(this.nodos.size()-1));
			puntosIntemediosAB.add(punto);			
		}
		//si va a la planta intermedia
		else {
			this.camion.setCargaActualGLP(this.camion.getTipo().getCapacidadGLP());
			this.camion.setCargaActualPetroleo(this.camion.getTipo().getCapacidadTanque());
			
			double distanciaPuntosAPI = Utils.calcularDistanciaTodosPuntos(puntosIntemediosAPI);
			long tiempoAPI = (long) (distanciaPuntosAPI/this.camion.getTipo().getVelocidadPromedio() * 3600);
			
			List<Punto> puntosIntemediosABPI = puntosIntemediosAPI.get(puntosIntemediosAPI.size()-1).getPuntosIntermedios(punto, this.fechaHoraTranscurrida.plusSeconds(tiempoAPI), this.camion, this.bloqueos);
			puntosIntemediosAB.addAll(puntosIntemediosAPI);
			puntosIntemediosAB.addAll(puntosIntemediosABPI);
			puntosIntemediosAB.add(punto);
		}

		
		
		double distanciaPuntosActualPedido = Utils.calcularDistanciaTodosPuntos(puntosIntemediosAB);
		long tiempo = (long) (distanciaPuntosActualPedido/this.camion.getTipo().getVelocidadPromedio() * 3600);
		
		LocalDateTime fechaHoraEntrega = this.fechaHoraTranscurrida.plusSeconds(tiempo);
		List<Punto> puntosIntemediosBC = punto.getPuntosIntermedios(planta, fechaHoraEntrega.plusMinutes(10), this.camion, this.bloqueos);
		puntosIntemediosBC.add(0, punto);
		puntosIntemediosBC.add(planta);
		double distanciaPuntosPedidoPlanta = Utils.calcularDistanciaTodosPuntos(puntosIntemediosBC);
		
		double consumoPetroleo = this.camion.calcularConsumoPetroleo(distanciaPuntosActualPedido+distanciaPuntosPedidoPlanta);
		
		
		if( (this.camion.getCargaActualPetroleo()>=consumoPetroleo) &&
				(fechaHoraEntrega.isBefore(pedido.getFechaLimite()))) {
			
			for (int i = 0; i < this.mantenimientos.size(); i++) {
				
					if(this.mantenimientos.get(i).getFechaInicio().compareTo(fechaHoraEntrega) < 0 && 
							this.mantenimientos.get(i).getFechaFin().compareTo(this.fechaHoraTranscurrida) > 0) {
						
						this.camion.setCargaActualGLP(cargaActualGLPOrig);
						this.camion.setCargaActualPetroleo(cargaActualPetOrig);
						
						
						if(this.nodos.get(this.nodos.size()-1).isPlanta()) {
							this.fechaHoraTranscurrida = this.mantenimientos.get(i).getFechaFin();
							return this.esFactible(pedido);
						}
						
						return puntosTotales;
						
					}
			}
			
			return puntosIntemediosAB;
		}
		
		this.camion.setCargaActualGLP(cargaActualGLPOrig);
		this.camion.setCargaActualPetroleo(cargaActualPetOrig);
		
		return puntosTotales;
			

	}
	


	@Override
	public int compareTo(RutaCompleta o) {
		if (this.getCostoRuta()>o.getCostoRuta()){
			return 1;
		}
		else {		
			return -1;
		}

	}
	
	public String to_string () {
		
		String str = "";
		str.concat("CAmion: " + this.camion.getCodigo()+ "\n");
		String format = "yyyy-MM-dd HH:mm:ss a";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		str.concat("fechaHoraTranscurrida: " + this.fechaHoraTranscurrida.format(formatter)+ "\n");
		str.concat("cantPedidosNoEntregados: " + this.cantPedidosNoEntregados+ "\n");
		str.concat("glpNoEntregado: " + this.glpNoEntregado+ "\n");
		str.concat("petroleoConsumido: " + this.petroleoConsumido+ "\n");
		str.concat("costoRuta: " + this.costoRuta+ "\n");
		str.concat("distanciaRecorrida: " + this.distanciaRecorrida+ "\n");
		str.concat("\n");
		str.concat("RUTAS\n");
		str.concat("\n");
		if (this.rutas != null) {			
			for (Ruta ruta: this.rutas) {
				str.concat("Size ruta: " + ruta.getPuntos().size() + "\n");
			}
		}
		return str;
	}
}