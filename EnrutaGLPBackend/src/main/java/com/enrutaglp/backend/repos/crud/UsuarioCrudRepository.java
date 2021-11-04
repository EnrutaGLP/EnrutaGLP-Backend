package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.enrutaglp.backend.tables.UsuarioTable;

public interface UsuarioCrudRepository extends CrudRepository<UsuarioTable, Integer>{

	
	@Query(   "SELECT * "
			+ "FROM usuario "
			+ "where correo = :correo"
			)
	List<UsuarioTable> listarPorCorreo(@Param("estado") String correo);
}
