package com.enrutaglp.backend.repos.jdbc;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
import com.enrutaglp.backend.utils.Utils;

@Component
public class JDBCBloqueoRepository implements BloqueoRepository {

	@Autowired
	BloqueoCrudRepository repo; 
	
	@Autowired
	PuntoCrudRepository puntoRepo;
	
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
	public List<Bloqueo> listarEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String desdeString = null,hastaString = null;
		if(fechaInicio!=null) {
			desdeString = fechaInicio.format(Utils.formatter1);
		}
		if(fechaFin!=null) {
			hastaString = fechaFin.format(Utils.formatter1);	
		}
		List<BloqueoTable> bloqueosBd; 
		if(desdeString == null && hastaString != null) {
			bloqueosBd = repo.listarBloqueosHasta(hastaString);
		} else if(desdeString !=null && hastaString == null) {
			bloqueosBd = repo.listarBloqueosDesde(desdeString);
		} else {
			bloqueosBd = repo.listarBloqueosEnRango(desdeString,hastaString);
		}
		
		List<Bloqueo> bloqueos = bloqueosBd.stream().map(bloqueoTable -> bloqueoTable.toModel()).collect(Collectors.toList());
		for(Bloqueo b: bloqueos) {
			b.setPuntos(((List<PuntoTable>)puntoRepo.listarPuntosPorIdBloqueo(b.getId())).stream()
					.map(puntoTable -> puntoTable.toModel()).collect(Collectors.toList()));
		}
		return bloqueos;
	}
	
}
