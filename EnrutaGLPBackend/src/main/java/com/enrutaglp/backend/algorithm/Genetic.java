package com.enrutaglp.backend.algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Planta;

public class Genetic {

	Population population; 
	Map<String,Pedido>pedidos; 
	List<Pedido> listaPedidos;
	Map<String,Camion>flota; 
	List<Bloqueo>bloqueos;
	Map<String,List<Mantenimiento>>mantenimientos; 
	List<Planta>plantas; 
	LocalDateTime fechaHoraActual; 
	
	public Genetic(Map<String,Pedido>pedidos, Map<String,Camion>flota, List<Bloqueo>bloqueos,
			Map<String,List<Mantenimiento>>mantenimientos,List<Planta> plantas, LocalDateTime fechaHoraActual) {
		this.plantas = plantas;
		this.bloqueos = bloqueos; 
		this.mantenimientos = mantenimientos; 
		this.pedidos = pedidos; 
		this.flota = flota; 
		this.listaPedidos = pedidos.values().stream().collect(Collectors.toList());
		this.fechaHoraActual = fechaHoraActual;
	}
	
	public Individual run(int maxIterNoImp, int numChildrenToGenerate, double wA, double wB, double wC, int mu, int epsilon,
			double percentGenesToMutate) {
		
		int nbIterNoImp = 1; 
		Individual childInd1,childInd2; 
		
		//Initialize population
		population = new Population(mu,epsilon,pedidos,flota,wA,wB,wC);
		
		boolean genNewBest; 
		for(int nbIter = 0; nbIterNoImp <= maxIterNoImp; nbIter++) {
			genNewBest = false;
			for(int i=0;i<numChildrenToGenerate;i++) {
				//Parent selection and crossover 
				Individual ind1 = population.getBinaryTournament(wA, wB, wC,mantenimientos, this.bloqueos, fechaHoraActual);
				Individual ind2 = population.getBinaryTournament(wA, wB, wC, mantenimientos, this.bloqueos, fechaHoraActual);
				childInd1 = crossover(ind1, ind2);
				//Apply mutation
				childInd2 = mutate(childInd1,percentGenesToMutate);
				//Evaluate new individuals
				childInd1.calcularFitness(wA, wB, wC,flota, mantenimientos, this.bloqueos, fechaHoraActual);
				childInd2.calcularFitness(wA, wB, wC,flota, mantenimientos, this.bloqueos, fechaHoraActual);
				boolean isNewBest = population.addIndividual(childInd1) || population.addIndividual(childInd2);
				genNewBest = (isNewBest)? isNewBest:genNewBest;
			}
			//Utils.printSolution(nbIter, population.getBest(),printWriter);
			if(genNewBest) nbIterNoImp = 1; 
			else nbIterNoImp++; 
		}
	    return population.getBest();
	    
	}
	
	public Individual crossover(Individual ind1, Individual ind2) {
		Individual childInd = new Individual(); 
		
		for(int i=0;i<listaPedidos.size();i++) {
			String codigo = listaPedidos.get(i).getCodigo();
			int choice = ThreadLocalRandom.current().nextInt(0, 2);
			if(choice==0) {
				childInd.addGene(codigo,ind1.getChromosome().get(codigo),listaPedidos.get(i));
			} else if(choice == 1) {
				childInd.addGene(codigo,ind2.getChromosome().get(codigo),listaPedidos.get(i));
			}
		}
		
		return childInd; 
	}
	
	public Individual mutate(Individual individual, double percentGenesToMutate) {
		Individual mutatedInd = new Individual();
		
		for(int i=0;i<listaPedidos.size();i++) {
			String codigo = listaPedidos.get(i).getCodigo();
			mutatedInd.addGene(codigo, individual.getChromosome().get(codigo),listaPedidos.get(i));
		}
		int numGenesToChange = (int)(listaPedidos.size()*percentGenesToMutate);
		for(int i=0;i<numGenesToChange;i++) {
			String pedido1 = listaPedidos.get(ThreadLocalRandom.current().nextInt(0, listaPedidos.size()))
					.getCodigo();
			String pedido2 = listaPedidos.get(ThreadLocalRandom.current().nextInt(0, listaPedidos.size()))
					.getCodigo();
			mutatedInd.swap(pedido1,pedido2);
		}
		return mutatedInd;
	}
}
