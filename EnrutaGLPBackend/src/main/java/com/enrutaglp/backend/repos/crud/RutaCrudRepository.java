package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.dtos.HojaRutaItemDTO;
import com.enrutaglp.backend.dtos.HojaRutaItemSinPuntosDTO;
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
			+ "c.codigo = :codigoCamion "
			+ "and "
			+ "p.orden >= p1.orden "
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
	
	@Query(   "SELECT MAX(orden) "
			+ "from ruta "
			+ "where id_camion = :idCamion")
	Integer listarOrdenDeLaUltimaRuta(@Param("idCamion")int idCamion);
	
	@Query( "SELECT "
			+ "r.id, "
			+ "c.codigo as codigo_camion, "
			+ "r.hora_salida, "
			+ "r.hora_llegada, "
			+ "r.consumo_petroleo, "
			+ "r.tipo, "
			+ "pe.codigo as codigo_pedido, "
			+ "ep.cantidad_entregada, "
			+ "pe.cantidad_glp, "
			+ "pe.fecha_limite, "
			+ "pl.nombre as nombre_planta, "
			+ "tc.capacidad_glp as cantidad_recargada "
			+ "FROM "
			+ "ruta r "
			+ "INNER JOIN "
			+ "camion c "
			+ "on c.id = r.id_camion "
			+ "INNER JOIN "
			+ "tipo_camion tc "
			+ "on tc.id = c.tipo "
			+ "LEFT JOIN "
			+ "entrega_pedido ep "
			+ "on ep.id_ruta = r.id "
			+ "LEFT JOIN "
			+ "recarga re "
			+ "on re.id_ruta = r.id "
			+ "LEFT JOIN "
			+ "pedido pe "
			+ "on pe.id = ep.id_pedido "
			+ "LEFT JOIN "
			+ "planta pl "
			+ "on pl.id = re.id_planta "
			+ "WHERE "
			+ "r.hora_llegada >= :fechaInicio"
			)
	List<HojaRutaItemSinPuntosDTO> listarHojasRutas(@Param("fechaInicio") String fechaInicio); 
}
