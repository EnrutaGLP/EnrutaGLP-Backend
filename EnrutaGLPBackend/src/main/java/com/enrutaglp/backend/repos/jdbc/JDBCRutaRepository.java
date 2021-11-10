package com.enrutaglp.backend.repos.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.enrutaglp.backend.enums.TipoRuta;
import com.enrutaglp.backend.models.EntregaPedido;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Ruta;
import com.enrutaglp.backend.repos.crud.BloqueoCrudRepository;
import com.enrutaglp.backend.repos.crud.EntregaPedidoCrudRepository;
import com.enrutaglp.backend.repos.crud.PedidoCrudRepository;
import com.enrutaglp.backend.repos.crud.PuntoCrudRepository;
import com.enrutaglp.backend.repos.crud.RecargaCrudRepository;
import com.enrutaglp.backend.repos.crud.RutaCrudRepository;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;
import com.enrutaglp.backend.repos.interfaces.RutaRepository;
import com.enrutaglp.backend.tables.EntregaPedidoTable;
import com.enrutaglp.backend.tables.PedidoTable;
import com.enrutaglp.backend.tables.PuntoTable;
import com.enrutaglp.backend.tables.RecargaTable;
import com.enrutaglp.backend.tables.RutaTable;

@Component
public class JDBCRutaRepository implements RutaRepository {

	@Autowired
	PuntoCrudRepository puntoRepo;
	
	@Autowired
	RutaCrudRepository rutaRepo;
	
	@Autowired
	EntregaPedidoCrudRepository entregaPedidoRepo;
	
	@Autowired
	RecargaCrudRepository recargaRepo;
	
	
	@Override
	public void registroMasivo(List<Ruta> rutas) {
		for(int i=0;i<rutas.size();i++) {
			rutas.get(i).setOrden(i+1);
			for(int j=0;j<rutas.get(i).getPuntos().size();j++) {
				rutas.get(i).getPuntos().get(j).setOrden(j+1);
			}
		}
		List<EntregaPedidoTable> entregas = new ArrayList<EntregaPedidoTable>();
		List<RecargaTable> recargas = new ArrayList<RecargaTable>();
		List<RutaTable> rutasTable = rutas.stream().map(r -> new RutaTable(r))
				.collect(Collectors.toList());
		for(int i=0;i<rutasTable.size();i++) {
			List<PuntoTable> puntos = rutas.get(i).getPuntos().stream().map(p->new PuntoTable(p))
					.collect(Collectors.toList());	
			for(PuntoTable p : puntos) {
				p.setIdBloqueo(rutasTable.get(i).getId());
			}
			puntoRepo.saveAll(puntos);
			if(rutas.get(i).getTipo()==TipoRuta.ENTREGA.getValue()) {
				Object o = rutas.get(i); 
				EntregaPedido ep = (EntregaPedido) o; 
				
			} else {
				
			}
		}
		rutaRepo.saveAll(rutasTable);
	}

}
