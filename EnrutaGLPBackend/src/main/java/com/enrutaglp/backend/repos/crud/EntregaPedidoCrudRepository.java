package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.dtos.EntregaPedidoDTO;
import com.enrutaglp.backend.tables.EntregaPedidoTable;

@Repository
public interface EntregaPedidoCrudRepository extends CrudRepository<EntregaPedidoTable, Integer>{
	
	@Query(   "SELECT ep.cantidad_entregada,"
			+ "p.* "
			+ "from ruta r "
			+ "inner join entrega_pedido ep "
			+ "on ep.id_ruta = r.id "
			+ "inner join pedido p "
			+ "on p.id = ep.id_pedido "
			+ "where r.orden >= :orden"
			+ "and r.id_camion = :idCamion"
	 		)
	List<EntregaPedidoDTO> listarEntregasPedidosFuturos(@Param("idCamion")int idCamion,@Param("orden")int orden );
	
	
}
