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
	
	@Query(  "SELECT * "
			+ "FROM pedido "
			+ "where "
			+ "pedido.cantidad_glp_por_planificar > 0 "
			+ "and "
			+ "pedido.fecha_pedido <= :hasta "
			+ "and "
			+ "pedido.fecha_pedido >= :desde "
			)
	List<PedidoTable>listarPedidosDesdeHasta(@Param("desde") String desde,@Param("hasta") String hasta); 
	
	
	
	@Query("SELECT pe.* from "
			+ "camion c "
			+ "inner join "
			+ "punto p "
			+ "on p.id = c.id_punto_actual "
			+ "inner join "
			+ "ruta r "
			+ "on r.id = p.id_ruta "
			+ "inner join "
			+ "entrega_pedido ep "
			+ "on ep.id_ruta = r.id "
			+ "inner join pedido pe "
			+ "on pe.id = ep.id_pedido")
	List<PedidoTable>listarEnRuta();
	
}
