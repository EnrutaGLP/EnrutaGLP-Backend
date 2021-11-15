package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.dtos.PuntoDTO;
import com.enrutaglp.backend.tables.RutaTable;

@Repository
public interface RutaCrudRepository extends CrudRepository<RutaTable, Integer>{

	//Falta:
	@Query("SELECT p.ubicacion_x, "
			+ "p.ubicacion_y, "
			+ "p.orden "
			+ "from camion c "
			+ "INNER JOIN punto p1 "
			+ "on p1.id = c.id_punto_actual "
			+ "INNER JOIN punto p "
			+ "on p.id_ruta = p1.id_ruta "
			+ "WHERE "
			+ "c.codigo = :codigoCamion"
			)
	List<PuntoDTO>listarPuntosDtoRutaActualCamion(@Param("codigoCamion") String codigoCamion);
	
	
	 @Query("SELECT r.* "
	 		+ "from ruta r "
	 		+ "inner join punto p "
	 		+ "on p.id_ruta = r.id "
	 		+ "inner join camion c "
	 		+ "on c.id_punto_actual = p.id "
	 		+ "where c.id = :idCamion"
	 		)
	RutaTable listarRutaActualCamion(@Param("idCamion")int idCamion);
}
