package com.enrutaglp.backend.repos.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.crud.CamionCrudRepository;
import com.enrutaglp.backend.repos.crud.PedidoCrudRepository;
import com.enrutaglp.backend.repos.interfaces.CamionRepository;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;
import com.enrutaglp.backend.tables.PedidoTable;

@Component
public class JDBCCamionRepository implements CamionRepository {

	@Autowired
	CamionCrudRepository repo;
	
	@Autowired
	JdbcTemplate template;


}
