package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.MantenimientoTable;

@Repository
public interface MantenimientoCrudRepository extends CrudRepository<MantenimientoTable, Integer>{
	@Query( "SELECT * "
			+ "FROM mantenimiento "
			+ "where "
			+ "fecha_fin >= :desde "
			+ "and fecha_inicio <= :hasta " 
			)
	List<MantenimientoTable>listarMatenimientoEnRango(@Param("desde") String desde,@Param("hasta") String hasta );
	
	@Query( "SELECT * "
			+ "FROM mantenimiento "
			+ "where "
			+ "fecha_fin >= :desde "
			)
	List<MantenimientoTable>listarMatenimientoDesde(@Param("desde") String desde);
	
	@Query(   "SELECT * "
			+ "FROM mantenimiento "
			+ "where "
			+ "fecha_inicio <= :hasta " 
			)
	List<MantenimientoTable>listarMatenimientoHasta(@Param("hasta") String hasta);
	
}
