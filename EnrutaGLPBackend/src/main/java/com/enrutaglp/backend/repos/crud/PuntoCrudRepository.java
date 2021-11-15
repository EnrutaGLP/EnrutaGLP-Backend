package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.dtos.PuntoSiguienteDTO;
import com.enrutaglp.backend.tables.BloqueoTable;
import com.enrutaglp.backend.tables.PedidoTable;
import com.enrutaglp.backend.tables.PuntoTable;

@Repository
public interface PuntoCrudRepository extends CrudRepository<PuntoTable, Integer>{
	
	@Query("SELECT * "
			+ "FROM punto "
			+ "where "
			+ "id_bloqueo = :idBloqueo "
			+ "order by orden"
			)
	List<PuntoTable>listarPuntosPorIdBloqueo(@Param("idBloqueo")int idBloqueo);
	
	
	//Falta:
	@Query("SELECT p.id as id, "
			+ "p.ubicacion_x as ubicacion_x,"
			+ "p.ubicacion_y as ubicacion_y, "
			+ "MAX(p.orden) as ultimo_orden"
			)
	List<PuntoSiguienteDTO>conseguirPuntoSiguienteEnrutado(@Param("idCamion")int idCamion);
}
