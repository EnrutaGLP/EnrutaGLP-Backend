package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.BloqueoTable;
import com.enrutaglp.backend.tables.PedidoTable;

@Repository
public interface BloqueoCrudRepository extends CrudRepository<BloqueoTable, Integer>{
	@Query(   "SELECT * "
			+ "FROM bloqueo "
			+ "where "
			+ "fecha_fin >= :desde "
			+ "and fecha_inicio <= :hasta "
			+ "order by fecha_inicio" 
			)
	List<BloqueoTable>listarBloqueosEnRango(@Param("desde") String desde,@Param("hasta") String hasta );
	
	@Query(   "SELECT * "
			+ "FROM bloqueo "
			+ "where "
			+ "fecha_fin >= :desde "
			+ "order by fecha_inicio"
			)
	List<BloqueoTable>listarBloqueosDesde(@Param("desde") String desde);
	
	@Query(   "SELECT * "
			+ "FROM bloqueo "
			+ "where "
			+ "fecha_inicio <= :hasta "
			+ "order by fecha_inicio" 
			)
	List<BloqueoTable>listarBloqueosHasta(@Param("hasta") String hasta );
	
	
}
