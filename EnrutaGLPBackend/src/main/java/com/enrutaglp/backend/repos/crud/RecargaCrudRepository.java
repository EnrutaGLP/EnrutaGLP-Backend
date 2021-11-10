package com.enrutaglp.backend.repos.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.RecargaTable;
import com.enrutaglp.backend.tables.RutaTable;

@Repository
public interface RecargaCrudRepository extends CrudRepository<RecargaTable, Integer>{

}
