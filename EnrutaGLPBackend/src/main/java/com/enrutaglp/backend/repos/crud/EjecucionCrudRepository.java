package com.enrutaglp.backend.repos.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.EjecucionTable;

@Repository
public interface EjecucionCrudRepository extends CrudRepository<EjecucionTable, Integer>{

}
