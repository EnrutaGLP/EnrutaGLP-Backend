package com.enrutaglp.backend.repos.crud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enrutaglp.backend.tables.AveriaTable;

@Repository
public interface AveriaCrudRepository extends CrudRepository<AveriaTable, Integer>{

}
