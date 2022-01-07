package com.enrutaglp.backend.repos.interfaces;

import java.util.List;
import java.util.Map;

import com.enrutaglp.backend.tables.ConfiguracionTable;

public interface ConfiguracionRepository {
	void actualizarLlave(String llave,String valor);
	void actualizarLlaves(Map<String,String>mapaConfiguracion);
	Map<String, String>listarConfiguracionCompleta();
}
