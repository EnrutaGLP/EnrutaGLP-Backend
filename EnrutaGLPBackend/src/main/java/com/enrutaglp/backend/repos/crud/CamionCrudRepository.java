package com.enrutaglp.backend.repos.crud;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.CamionTable;
import com.enrutaglp.backend.tables.PedidoTable;

@Repository
public interface CamionCrudRepository extends CrudRepository<CamionTable, Integer>{
	
}
