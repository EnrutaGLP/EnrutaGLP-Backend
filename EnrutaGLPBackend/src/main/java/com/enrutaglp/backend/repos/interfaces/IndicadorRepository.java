package com.enrutaglp.backend.repos.interfaces;

import java.util.List;
import java.util.Map;

import com.enrutaglp.backend.tables.ConfiguracionTable;

public interface IndicadorRepository {
	void actualizarIndicador(String nombre,Double valor);
	void actualizarIndicadores(List<Integer>pedidosIdsActualizar);
	Map<String, Double>listarIndicadores();
}
