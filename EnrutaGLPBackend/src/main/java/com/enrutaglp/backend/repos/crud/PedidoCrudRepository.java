package com.enrutaglp.backend.repos.crud;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.PedidoTable;

@Repository
public interface PedidoCrudRepository extends CrudRepository<PedidoTable, Integer>{
	
	/*@Query( "INSERT INTO PEDIDO "
		  + "(codigo,"
		  + "cliente,"
		  + "cantidad_glp,"
		  + "cantidad_glp_atendida,"
		  + "ubicacion_x,"
		  + "ubicacion_y,"
		  + "fecha_pedido,"
		  + "fecha_limite) "
		  + "VALUES("
		  + ":codigo,"
		  + ":cliente,"
		  + ":cantidad_glp,"
		  + ":cantidad_glp_atendida,"
		  + ":ubicacion_x,"
		  + ":ubicacion_y,"
		  + ":fecha_pedido,"
		  + ":fecha_limite"
		  + ")"
			)
	void registrar(@Param("codigo") String codigo,@Param("cliente") String cliente,@Param("cantidad_glp") double cantidadGlp,
			@Param("cantidad_glp_atendida") double cantidadGlpAtendida,@Param("ubicacion_x") int ubicacionX,@Param("ubicacion_y") int ubicacionY,
			);*/
}
