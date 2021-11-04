package com.enrutaglp.backend.repos.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.crud.CamionCrudRepository;
import com.enrutaglp.backend.repos.interfaces.CamionRepository;
import com.enrutaglp.backend.tables.CamionTable;

@Component
public class JDBCCamionRepository implements CamionRepository {

	@Autowired
	CamionCrudRepository repo;
	
	@Autowired
	JdbcTemplate template;

	
	
	@Override
	public List<Camion> listar() {
		List<Camion> camiones = ((List<CamionTable>)repo.findAll()).stream()
				.map(camionTable -> camionTable.toModel()).collect(Collectors.toList());
		return camiones;
	}

	@Override
	public void registrar(Camion camion) {
		
	}

	@Override
	public int listarIDporCodigo(String codigoCamion) {

		return repo.listarIDporCodigo(codigoCamion).get(0).getId();
	}

	@Override
	public Map<String, Camion> listarDisponiblesParaEnrutamiento() {
		List<Camion> camiones = ((List<CamionTable>)repo.findAll()).stream()
				.map(camionTable -> camionTable.toModel()).collect(Collectors.toList());
		Map<String,Camion> camionesMapa = new HashMap<String, Camion>();
		for(Camion c: camiones) {
			camionesMapa.put(c.getCodigo(), c);
		}
		return camionesMapa;
	}

}
