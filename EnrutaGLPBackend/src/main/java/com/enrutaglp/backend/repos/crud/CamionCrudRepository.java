package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.CamionTable;

@Repository
public interface CamionCrudRepository extends CrudRepository<CamionTable, Integer>{
	
	
	@Query(   "SELECT id "
			+ "FROM camion "
			+ "where codigo = :codigoCamion"
			)
	List<CamionTable> listarIDporCodigo(@Param("codigoCamion") String codigoCamion);
}
