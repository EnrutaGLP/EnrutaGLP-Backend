package com.enrutaglp.backend.repos.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.enrutaglp.backend.enums.TipoRuta;
import com.enrutaglp.backend.models.EntregaPedido;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Recarga;
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
			//en verdad deberia ser a partir de la ultima dada
			rutas.get(i).setOrden(i+1);
			for(int j=0;j<rutas.get(i).getPuntos().size();j++) {
				rutas.get(i).getPuntos().get(j).setOrden(j+1);
			}
		}
		List<EntregaPedidoTable> entregas = new ArrayList<EntregaPedidoTable>();
		List<RecargaTable> recargas = new ArrayList<RecargaTable>();
		List<RutaTable> rutasTable = rutas.stream().map(r -> new RutaTable(r))
				.collect(Collectors.toList());
		List<PuntoTable> puntos = new ArrayList<PuntoTable>();
		rutaRepo.saveAll(rutasTable);
		for(int i=0;i<rutasTable.size();i++) {
			List<PuntoTable>puntosDeLaRuta = rutas.get(i).getPuntos().stream().map(p->new PuntoTable(p))
					.collect(Collectors.toList());	
			for(PuntoTable p : puntosDeLaRuta) {
				p.setIdRuta(rutasTable.get(i).getId());
			}
			Object o = rutas.get(i); 
			if(rutas.get(i).getTipo()==TipoRuta.ENTREGA.getValue()) {
				EntregaPedido ep = (EntregaPedido) o; 
				entregas.add(new EntregaPedidoTable(rutasTable.get(i).getId(),ep));
			} else if(rutas.get(i).getTipo()==TipoRuta.RECARGA.getValue()){
				Recarga r = (Recarga) o; 
				recargas.add(new RecargaTable(rutasTable.get(i).getId(),r));
			}
			puntos = Stream.concat(puntos.stream(), puntosDeLaRuta.stream()).collect(Collectors.toList());
		}
		puntoRepo.saveAll(puntos);
		entregaPedidoRepo.saveAll(entregas);
		recargaRepo.saveAll(recargas);
	}

}
