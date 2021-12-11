package com.enrutaglp.backend.repos.jdbc;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.repos.crud.EjecucionCrudRepository;
import com.enrutaglp.backend.repos.interfaces.EjecucionRepository;
import com.enrutaglp.backend.tables.EjecucionTable;
@Component
public class JDBCEjecucionRepository implements EjecucionRepository{

	@Autowired
	EjecucionCrudRepository repo; 
	
	@Override
	public void registrar(int modoEjecucion, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		repo.save(new EjecucionTable(modoEjecucion, fechaInicio, fechaFin));
	}

}
