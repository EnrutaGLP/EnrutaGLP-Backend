package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.PedidoTable;

@Repository
public interface PedidoCrudRepository extends CrudRepository<PedidoTable, Integer>{
	
	@Query(   "SELECT * "
			+ "FROM pedido "
			+ "where estado = :estado"
			)
	List<PedidoTable>listarPorEstado(@Param("estado") byte estado);

	@Query(  "SELECT * "
			+ "FROM pedido "
			+ "where "
			+ "pedido.cantidad_glp_por_planificar > 0 "
			+ "and "
			+ "pedido.fecha_pedido <= :hasta"
			)
	List<PedidoTable>listarPendientesPlanificacion(@Param("hasta") String hasta); 
	
}
