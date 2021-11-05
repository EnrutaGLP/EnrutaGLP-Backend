package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.dtos.CamionTipoDTO;
import com.enrutaglp.backend.tables.CamionTable;

@Repository
public interface CamionCrudRepository extends CrudRepository<CamionTable, Integer>{
	
	
	@Query(   "SELECT id "
			+ "FROM camion "
			+ "where codigo = :codigoCamion"
			)
	List<CamionTable> listarIDporCodigo(@Param("codigoCamion") String codigoCamion);
	
	@Query( "SELECT "
			+ "c.id as id, "
			+ "c.codigo as codigo, "
			+ "c.placa as placa, "
			+ "c.ubicacion_actual_x , "
			+ "c.ubicacion_actual_y ,"
			+ "c.carga_actual_glp as cargaActualGLP, "
			+ "c.carga_actual_petroleo, "
			+ "c.estado as estado, "
			+ "c.tipo as tipo, "
			+ "tc.id as tipo_camion_id, "
			+ "tc.tara as tara, "
			+ "tc.peso_bruto, "
			+ "tc.capacidad_glp, "
			+ "tc.peso_glp, "
			+ "tc.peso_combinado, "
			+ "tc.capacidad_tanque, "
			+ "tc.velocidad_promedio, "
			+ "tc.unidades "
			+ "FROM "
			+ "camion c "
			+ "INNER JOIN "
			+ "tipo_camion tc "
			+ "ON "
			+ "tc.id = c.tipo"
			)
	List<CamionTipoDTO> listarCamionesTipoDTODisponibles();
}
