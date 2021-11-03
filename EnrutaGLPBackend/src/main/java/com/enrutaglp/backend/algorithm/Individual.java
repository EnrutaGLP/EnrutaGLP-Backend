package com.enrutaglp.backend.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.EntregaPedido;
import com.enrutaglp.backend.models.Pedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Individual implements Comparable<Individual> {

	private Map<String, List<EntregaPedido>> entregas;
	private Map<String, Map<String, Integer>> chromosome;
	private Map<String, Ruta> rutas;
	private Map<String, Map<String, Pedido>> asignacionesCamiones;
	private List<Camion> camiones;
	private double consumoTotalPetroleo = 0; // suma de consumo de todas las entregas
	private int cantidadPedidosNoEntregados;
	private double cantidadGlpNoEntregado;
	private byte seEstanEntregandoATiempo = 1; // 1 si todos se entregan a tiempo, 0 si no
	private int minutosAdicional = 0; // suma de minutos en los que no se entregan a tiempo los pedido
	private double fitness;

	public Individual() {
		this.entregas = new HashMap<String, List<EntregaPedido>>();
		this.chromosome = new HashMap<String, Map<String, Integer>>();
		this.asignacionesCamiones = new HashMap<String, Map<String, Pedido>>();
		this.rutas = new HashMap<String, Ruta>();
	}

	public Individual(Map<String, Pedido> pedidos, Map<String, Camion> flota) {
		this.entregas = new HashMap<String, List<EntregaPedido>>();
		this.chromosome = new HashMap<String, Map<String, Integer>>();
		this.asignacionesCamiones = new HashMap<String, Map<String, Pedido>>();
		this.rutas = new HashMap<String, Ruta>();
		this.generateRandomIndividual(pedidos, flota);
	}

	public void insertarEntregaPedido(EntregaPedido entregaPedido) {

		List<EntregaPedido> entregasPedidos = this.entregas.get(entregaPedido.getPedido().getCodigo());

		if (entregasPedidos == null) {
			entregasPedidos = new ArrayList<EntregaPedido>();
			entregasPedidos.add(entregaPedido);
			this.entregas.put(entregaPedido.getPedido().getCodigo(), entregasPedidos);
		} else {
			this.entregas.get(entregaPedido.getPedido().getCodigo()).add(entregaPedido);
		}
	}

	public void generateRandomIndividual(Map<String, Pedido> pedidos, Map<String, Camion> flota) {
		List<Pedido> listaPedidos = pedidos.values().stream().collect(Collectors.toList());
		List<Camion> listaFlota = flota.values().stream().collect(Collectors.toList());

		for (int i = 0; i < listaPedidos.size(); i++) {
			String key = listaPedidos.get(i).getCodigo();
			int randomCamionIndex = ThreadLocalRandom.current().nextInt(0, listaFlota.size());
			
			//System.out.println(randomCamionIndex);
			
			Map<String, Integer> value = new HashMap<String, Integer>();
			String codigoCamion = listaFlota.get(randomCamionIndex).getCodigo();
			value.put(codigoCamion, 0);
			if (!asignacionesCamiones.containsKey(codigoCamion)) {
				Map<String, Pedido> mapCamion = new HashMap<String, Pedido>();
				asignacionesCamiones.put(codigoCamion, mapCamion);
			}
			asignacionesCamiones.get(codigoCamion).put(key, new Pedido(listaPedidos.get(i)));
			chromosome.put(key, value);
		}
	}
	
	public void addGene(String key, Map<String, Integer> value, Pedido pedido) {
		String codigoCamion = (String) value.keySet().stream().findFirst().get();
		if (!asignacionesCamiones.containsKey(codigoCamion)) {
			Map<String, Pedido> mapCamion = new HashMap<String, Pedido>();
			asignacionesCamiones.put(codigoCamion, mapCamion);
		}
		asignacionesCamiones.get(codigoCamion).put(key, new Pedido(pedido));
		//hacer una copia de value
		Map<String, Integer> copia = copiarGen(value);
		chromosome.put(key, copia);
	}

	public Map<String, Integer> copiarGen(Map<String,Integer> genOriginal){
		Map<String, Integer> copia = new HashMap<String, Integer>();
		copia.put(genOriginal.keySet().stream().findFirst().get(),
				genOriginal.get(genOriginal.keySet().stream().findFirst().get()));
		return copia; 
		
	}
	public int getSize() {
		return chromosome.keySet().size();
	}

	public void swap(String codigoPedido1, String codigoPedido2) {
		if(codigoPedido1==codigoPedido2){
			return; 
		}
		Map<String, Integer> valuePedido1 = chromosome.get(codigoPedido1);
		Map<String, Integer> valuePedido2 = chromosome.get(codigoPedido2);

		String codigoCamion1 = valuePedido1.keySet().stream().findFirst().get();
		String codigoCamion2 = valuePedido2.keySet().stream().findFirst().get();
		if(codigoCamion1==codigoCamion2) {
			return;
		}
		Pedido pedido1 = new Pedido(asignacionesCamiones.get(codigoCamion1).get(codigoPedido1));
		Pedido pedido2 = new Pedido(asignacionesCamiones.get(codigoCamion2).get(codigoPedido2));

		chromosome.replace(codigoPedido1, copiarGen(valuePedido2));
		chromosome.replace(codigoPedido2, copiarGen(valuePedido1));

		asignacionesCamiones.get(codigoCamion1).remove(codigoPedido1);
		asignacionesCamiones.get(codigoCamion1).put(codigoPedido2, pedido2);

		asignacionesCamiones.get(codigoCamion2).remove(codigoPedido2);
		asignacionesCamiones.get(codigoCamion2).put(codigoPedido1, pedido1);

	}

	public double calcularFitness(double wA, double wB, double wC, Map<String, Camion> flota) {
		this.fitness = 0.0;
		this.cantidadPedidosNoEntregados = 0; 
		this.consumoTotalPetroleo = 0.0; 
		rutas = new HashMap<String, Ruta>();
		for (Map.Entry<String, Map<String, Pedido>> entry : asignacionesCamiones.entrySet()) {
			
			Grasp grasp = new Grasp(entry.getValue(), flota.get(entry.getKey()), "12/09/2021", "20:00", wA, wB, wC);
			
			Ruta ruta = grasp.run(10);
			
			rutas.put(entry.getKey(), ruta);
			
			fitness += ruta.getCostoRuta();
			
			consumoTotalPetroleo += ruta.getPetroleoConsumido(); 
			
			cantidadPedidosNoEntregados += ruta.getCantPedidosNoEntregados();
			
		}
		
		return fitness;
	}

	@Override
	public int compareTo(Individual o) {
		if(this.getFitness()>o.getFitness()) {
			return 1; 
		}
		else {
			return -1;
		}
	}

}