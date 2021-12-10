package com.enrutaglp.backend.repos.jdbc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.enums.EstadoCamion;
import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.repos.crud.AveriaCrudRepository;
import com.enrutaglp.backend.repos.crud.CamionCrudRepository;
import com.enrutaglp.backend.repos.crud.MantenimientoCrudRepository;
import com.enrutaglp.backend.repos.interfaces.AveriaRepository;
import com.enrutaglp.backend.repos.interfaces.MantenimientoRepository;
import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.CamionTable;
import com.enrutaglp.backend.tables.MantenimientoTable;
import com.enrutaglp.backend.utils.Utils;


@Component
public class JDBCMantenimientoRepository implements MantenimientoRepository{
	
	@Autowired
	MantenimientoCrudRepository repo;
	
	@Autowired
	CamionCrudRepository camionRepo; 

	
	@Value("${plantas.principal.x}")
	private int plantaPrincipalX;
	
	@Value("${plantas.principal.y}")
	private int plantaPrincipalY;
	
	@Override
	public Map<String, List<Mantenimiento>> obtenerMapaDeMantenimientos(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		Map<String, List<Mantenimiento>> mapa = new HashMap<String, List<Mantenimiento>>(); 
		List<CamionTable>camiones = (List<CamionTable>) camionRepo.findAll(); 
		Map<Integer, String> mapaIdCodigo = new HashMap<Integer, String>();
		String desdeString = null,hastaString = null;
		if(fechaInicio!=null) {
			desdeString = fechaInicio.format(Utils.formatter1);
		}
		if(fechaFin!=null) {
			hastaString = fechaFin.format(Utils.formatter1);	
		}
		
		List<MantenimientoTable> mantenimientosBd;
		if(desdeString == null && hastaString != null) {
			mantenimientosBd = repo.listarMatenimientoHasta(hastaString);
		} else if(desdeString !=null && hastaString == null) {
			mantenimientosBd = repo.listarMatenimientoDesde(desdeString);
		} else {
			mantenimientosBd = repo.listarMatenimientoEnRango(desdeString, hastaString);
		}
		List<Mantenimiento> mantenimientos = mantenimientosBd.stream().map(mant -> mant.toModel()).collect(Collectors.toList());
		
		for(CamionTable c: camiones) {
			mapa.put(c.getCodigo(),new ArrayList<Mantenimiento>());
			mapaIdCodigo.put(c.getId(), c.getCodigo());
		}
		
		//Comentar lo siguiente para no considerar mantenimientos:
		/*
		for(Mantenimiento m: mantenimientos) {
			String codigo = mapaIdCodigo.get(m.getId()); 
			mapa.get(codigo).add(m);
		}
		*/
		
		return mapa; 
	}

	@Override
	public void registrarMantenimiento(Mantenimiento mantenimiento) {
		repo.save(new MantenimientoTable(mantenimiento,true));
	}
	
}
