package com.enrutaglp.backend.repos.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.repos.crud.AveriaCrudRepository;
import com.enrutaglp.backend.repos.crud.ConfiguracionCrudRepository;
import com.enrutaglp.backend.repos.interfaces.AveriaRepository;
import com.enrutaglp.backend.repos.interfaces.ConfiguracionRepository;
import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.ConfiguracionTable;


@Component
public class JDBCConfiguracionRepository implements ConfiguracionRepository{
	
	@Autowired
	ConfiguracionCrudRepository repo;

	@Override
	public void actualizarLlave(String llave, String valor) {
		ConfiguracionTable configuracionTable = new ConfiguracionTable(llave,valor); 
		repo.save(configuracionTable);
	}

	@Override
	public Map<String, String>listarConfiguracionCompleta() {
		List<ConfiguracionTable> configuraciones = (List<ConfiguracionTable>) repo.findAll();
		Map<String, String> configuracionMapa = new HashMap<String, String>();
		for(ConfiguracionTable c : configuraciones) {
			configuracionMapa.put(c.getLlave(),c.getValor());
		}
		return configuracionMapa;
	}

	@Override
	public void actualizarLlaves(Map<String, String> mapaConfiguracion) {
		List<ConfiguracionTable> configuraciones = new ArrayList<ConfiguracionTable>();
		for (Map.Entry<String,String> entry : mapaConfiguracion.entrySet()) {
			configuraciones.add(new ConfiguracionTable(entry.getKey(),entry.getValue())); 
		}
        repo.saveAll(configuraciones);
	} 
	
	
}
