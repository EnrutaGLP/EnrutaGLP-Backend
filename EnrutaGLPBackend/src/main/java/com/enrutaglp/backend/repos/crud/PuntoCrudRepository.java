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
	@Query(	  "SELECT "
			+ "IFNULL(p2.id,IFNULL(p3.id,p4.id)) as id, "
			+ "IFNULL(p2.ubicacion_x,IFNULL(p3.ubicacion_x,p4.ubicacion_x)) as ubicacion_x, "
			+ "IFNULL(p2.ubicacion_y,IFNULL(p3.ubicacion_y,p4.ubicacion_y)) as ubicacion_y "
			+ "from camion c "
			+ "left join punto p "
			+ "on p.id = c.id_punto_actual "
			+ "left join punto p2 "
			+ "on p2.orden = p.orden + 1 "
			+ "and p2.id_ruta = p.id_ruta "
			+ "left join ruta r "
			+ "on r.id = p.id_ruta "
			+ "left join ruta r2 "
			+ "on r2.orden = r.orden + 1 "
			+ "and r2.id_camion = c.id "
			+ "left join punto p3 "
			+ "on p3.id_ruta = r2.id "
			+ "and p3.orden = 1 "
			+ "left join ruta r3 "
			+ "on r3.id_camion = c.id "
			+ "and r3.orden = 1 "
			+ "left join punto p4 "
			+ "on p4.id_ruta = r3.id "
			+ "and p4.orden = 1 "
			+ "where "
			+ "c.id = :idCamion"
			)
	PuntoSiguienteDTO conseguirPuntoSiguienteEnrutado(@Param("idCamion")int idCamion);
}
