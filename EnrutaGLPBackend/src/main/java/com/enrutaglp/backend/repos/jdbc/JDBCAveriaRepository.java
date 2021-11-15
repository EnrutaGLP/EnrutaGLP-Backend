package com.enrutaglp.backend.repos.jdbc;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.enums.EstadoCamion;
import com.enrutaglp.backend.enums.TipoMantenimiento;
import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.repos.crud.AveriaCrudRepository;
import com.enrutaglp.backend.repos.crud.CamionCrudRepository;
import com.enrutaglp.backend.repos.crud.MantenimientoCrudRepository;
import com.enrutaglp.backend.repos.interfaces.AveriaRepository;
import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.CamionTable;
import com.enrutaglp.backend.utils.Utils;


@Component
public class JDBCAveriaRepository implements AveriaRepository{
	
	@Autowired
	AveriaCrudRepository repo;
	
	@Autowired
	CamionCrudRepository camionRepo; 
	
	@Autowired
	MantenimientoCrudRepository mantRepo;
	
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
