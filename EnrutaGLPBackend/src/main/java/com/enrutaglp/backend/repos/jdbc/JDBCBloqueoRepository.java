package com.enrutaglp.backend.repos.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.repos.crud.BloqueoCrudRepository;
import com.enrutaglp.backend.repos.crud.PuntoCrudRepository;
import com.enrutaglp.backend.repos.interfaces.BloqueoRepository;
import com.enrutaglp.backend.tables.BloqueoTable;
import com.enrutaglp.backend.tables.PuntoTable;

@Component
public class JDBCBloqueoRepository implements BloqueoRepository {

	@Autowired
	BloqueoCrudRepository repo; 
	
	@Autowired
	PuntoCrudRepository puntoRepo;
	
	@Autowired
	JdbcTemplate template;
	
	@Override
	public void registroMasivo(List<Bloqueo> bloqueos) {
		List<BloqueoTable> bloqueosTables = bloqueos.stream().map(b->new BloqueoTable(b))
				.collect(Collectors.toList());
		
		repo.saveAll(bloqueosTables);
		for(int i=0;i<bloqueosTables.size();i++) {
			List<PuntoTable> puntos = bloqueos.get(i).getPuntos().stream().map(p-> new PuntoTable(p))
					.collect(Collectors.toList());	
			for(PuntoTable p : puntos) {
				p.setIdBloqueo(bloqueosTables.get(i).getId());
			}
			puntoRepo.saveAll(puntos);
		}
	}
	
}
