package com.enrutaglp.backend.repos.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.repos.crud.AveriaCrudRepository;
import com.enrutaglp.backend.repos.interfaces.AveriaRepository;
import com.enrutaglp.backend.tables.AveriaTable;


@Component
public class JDBCAveriaRepository implements AveriaRepository{
	
	@Autowired
	AveriaCrudRepository repo;
	
	@Autowired
	JdbcTemplate template;
	
	
	@Override
	public void registrar(Averia averia) {
		AveriaTable averiaTable = new AveriaTable(averia, true);
		String sql = "INSERT INTO averia(id, id_camion, fecha) VALUES(?,?,?);";
		try {
			template.update(sql,averiaTable.getId(), averiaTable.getIdCamion(), averiaTable.getFecha());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
