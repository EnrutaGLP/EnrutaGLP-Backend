package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.dtos.PuntoDTO;
import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.RutaTable;

@Repository
public interface RutaCrudRepository extends CrudRepository<RutaTable, Integer>{

	@Query("SELECT p.ubicacion_x as ubicacion_x, "
			+ "p.ubicacion_y as ubicacion_y, "
			+ "p.orden as orden "
			+ "FROM "
			+ "camion c "
			+ "LEFT JOIN punto p "
			+ "on p.id = c.id_punto_actual "
			+ "LEFT JOIN ruta r "
			+ "on r.id = p.id_ruta "
			+ ""
			)
	List<PuntoDTO>listarPuntosDtoRutaActualCamion(@Param("idCamion") int idCamion);
}
