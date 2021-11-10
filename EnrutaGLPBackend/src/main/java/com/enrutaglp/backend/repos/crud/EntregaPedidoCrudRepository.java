package com.enrutaglp.backend.repos.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.EntregaPedidoTable;
import com.enrutaglp.backend.tables.RutaTable;

@Repository
public interface EntregaPedidoCrudRepository extends CrudRepository<EntregaPedidoTable, Integer>{

}
