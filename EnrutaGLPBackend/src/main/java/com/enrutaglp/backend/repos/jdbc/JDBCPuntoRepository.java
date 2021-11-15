package com.enrutaglp.backend.repos.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.dtos.PuntoSiguienteDTO;
import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.repos.crud.AveriaCrudRepository;
import com.enrutaglp.backend.repos.crud.ConfiguracionCrudRepository;
import com.enrutaglp.backend.repos.interfaces.AveriaRepository;
import com.enrutaglp.backend.repos.interfaces.ConfiguracionRepository;
import com.enrutaglp.backend.repos.interfaces.PuntoRepository;
import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.ConfiguracionTable;


@Component
public class JDBCPuntoRepository implements PuntoRepository{

	@Override
	public List<PuntoSiguienteDTO> conseguirPuntoSiguienteEnrutado(int idCamion) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
