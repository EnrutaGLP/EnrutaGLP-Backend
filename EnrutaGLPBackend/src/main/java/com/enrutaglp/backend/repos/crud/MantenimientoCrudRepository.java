package com.enrutaglp.backend.repos.crud;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.MantenimientoTable;

@Repository
public interface MantenimientoCrudRepository extends CrudRepository<MantenimientoTable, Integer>{
	//List<MantenimientoTable>listarMatenimiento()
}
