package com.enrutaglp.backend.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.models.Pedido;

public class Population {
	private List<Individual>individuals; 
	private Individual best;
	private int size;
	private int mu; 
	private int epsilon; 
	private Map<String,Camion>flota;
	private double wA; 
	private double wB; 
	private double wC; 
	private double probability = 0.5;
	public Population(int mu, int epsilon, Map<String,Pedido>pedidos, Map<String,Camion>flota,
			double wA,double wB, double wC) {
		this.mu = mu; 
		this.epsilon = epsilon;
		this.wA = wA; 
		this.wB = wB; 
		this.wC = wC;
		this.best = null;
		this.flota = flota;
		generatePopulation(pedidos);
	}
	
	public void generatePopulation(Map<String,Pedido>pedidos) {
		this.individuals = new ArrayList<Individual>();
		for(int i=0; i<mu;i++) {
			Individual individual = new Individual(pedidos,flota);
			individuals.add(individual);
		}
		this.size = mu;
	}

	public void applySurvivorSelection() { 
		int tournamentSize = mu/3;
		while(size>mu) {
			
			final int[] ints = new Random().ints(0, size).distinct().limit(tournamentSize).toArray();
			List<Individual> selectedForTournament = new ArrayList<Individual>();
			for(int i=0;i<tournamentSize;i++) {
				selectedForTournament.add(individuals.get(ints[i]));
			}
			Collections.sort(selectedForTournament);
			double factor = 1; 
			double currentProb = probability;
			List<Individual> selectedForRemoval = new ArrayList<Individual>();
			for(int i=0;i<tournamentSize;i++) {
				factor *= Math.pow(1-probability, i);
				currentProb *= factor;
				double randomValue = ThreadLocalRandom.current().nextDouble(0, 1);
				if(selectedForTournament.get(i) != best && randomValue > currentProb) {
					selectedForRemoval.add(selectedForTournament.get(i));
					size--; 
				}
				if(size==mu)break;
			}
			//Remover individuos que fueron elegidos
			individuals.removeAll(selectedForRemoval);
		}
		
	}
	
	
	
	public boolean addIndividual(Individual individual) {
		boolean isBest = false;
		individuals.add(individual);
		size++;
		if(best == null || best.getFitness() > individual.getFitness()) {
			best = individual;
			isBest = true; 
		}
		if(size>(mu+epsilon)) {
			applySurvivorSelection();
		}
		return isBest;
	}
	
	public Individual getBinaryTournament(double wA, double wB, double wC, Map<String,List<Mantenimiento>>mantenimientos) {
		int place1, place2; 
		while(true) {
			place1 = ThreadLocalRandom.current().nextInt(0, size);
			place2 = ThreadLocalRandom.current().nextInt(0, size);
			if(place1!=place2)break; 
		}
		
		Individual ind1 = individuals.get(place1); 
		Individual ind2 = individuals.get(place2); 
		//return the one with the lowest fitness 
		
		double fitness1 = ind1.calcularFitness(wA, wB, wC,flota, mantenimientos);
		//System.out.println("Todo correcto hasta aqui fitness1");
		
		double fitness2 = ind2.calcularFitness(wA, wB, wC,flota, mantenimientos);
		//System.out.println("Todo correcto hasta aqui fitness2");
		//System.out.println("Todo correcto hasta aqui");
		//System.exit(0);	

		
		Individual ind3 = (fitness1 > fitness2)? ind2 : ind1;
		
		return ind3;
	}

	public Individual getBest() {
		return best;
	}

	public void setBest(Individual best) {
		this.best = best;
	}
}
