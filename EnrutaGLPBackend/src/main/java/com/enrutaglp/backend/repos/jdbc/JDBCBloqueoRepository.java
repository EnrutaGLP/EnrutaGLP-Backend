package com.enrutaglp.backend.repos.jdbc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.crud.BloqueoCrudRepository;
import com.enrutaglp.backend.repos.crud.PuntoCrudRepository;
import com.enrutaglp.backend.repos.interfaces.BloqueoRepository;
import com.enrutaglp.backend.tables.BloqueoTable;
import com.enrutaglp.backend.tables.PedidoTable;
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

	@Override
	public List<Bloqueo> listarEnRango(Date fechaInicio, Date fechaFin) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String desdeString = sdf.format(fechaInicio);
		String hastaString = sdf.format(fechaFin);
		List<Bloqueo> bloqueos = ((List<BloqueoTable>)repo.listarBloqueosEnRango(desdeString,hastaString)).stream()
				.map(bloqueoTable -> bloqueoTable.toModel()).collect(Collectors.toList());
		for(Bloqueo b: bloqueos) {
			
		}
		return null;
	}
	
}
