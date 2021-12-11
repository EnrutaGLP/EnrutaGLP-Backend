package com.enrutaglp.backend.repos.interfaces;

import java.util.List;
import java.util.Map;

import com.enrutaglp.backend.tables.ConfiguracionTable;

public interface IndicadorRepository {
	void actualizarIndicador(String nombre,Double valor);
	void actualizarIndicadoresConPedidos(List<Integer>pedidosIdsActualizar);
	void actualizarIndicadores(Map<String, Double> mapa); 
	void resetearIndicadores(); 
	Map<String, Double>listarIndicadores();
}
