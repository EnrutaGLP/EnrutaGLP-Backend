package com.enrutaglp.backend.repos.interfaces;

import java.util.List;
import java.util.Map;

import com.enrutaglp.backend.tables.ConfiguracionTable;

public interface ConfiguracionRepository {
	void actualizarLlave(String llave,String valor);
	Map<String, String>listarConfiguracionCompleta();
}
