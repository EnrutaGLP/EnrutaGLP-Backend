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

import com.enrutaglp.backend.dtos.ListaRutasActualesDTO;
import com.enrutaglp.backend.dtos.CamionRutaDTO;
import com.enrutaglp.backend.enums.EstadoCamion;
import com.enrutaglp.backend.enums.TipoRuta;
import com.enrutaglp.backend.models.EntregaPedido;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Punto;
import com.enrutaglp.backend.models.Recarga;
import com.enrutaglp.backend.models.Ruta;
import com.enrutaglp.backend.repos.crud.BloqueoCrudRepository;
import com.enrutaglp.backend.repos.crud.CamionCrudRepository;
import com.enrutaglp.backend.repos.crud.EntregaPedidoCrudRepository;
import com.enrutaglp.backend.repos.crud.PedidoCrudRepository;
import com.enrutaglp.backend.repos.crud.PuntoCrudRepository;
import com.enrutaglp.backend.repos.crud.RecargaCrudRepository;
import com.enrutaglp.backend.repos.crud.RutaCrudRepository;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;
import com.enrutaglp.backend.repos.interfaces.RutaRepository;
import com.enrutaglp.backend.tables.CamionTable;
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
	CamionCrudRepository camionRepo;
	
	@Autowired
	EntregaPedidoCrudRepository entregaPedidoRepo;
	
	@Autowired
	RecargaCrudRepository recargaRepo;
	
	@Override
	public void registroMasivo(int camionId,List<Ruta> rutas) {
		int i = 0; 
		for(Ruta r : rutas) {
			//en verdad deberia ser a partir de la ultima dada
			r.setOrden(i+1);
			
			for(int j=0;j<r.getPuntos().size();j++) {
				
				if(j < (r.getPuntos().size()-1) && 
				   (r.getPuntos().get(j).getUbicacionX() != r.getPuntos().get(j+1).getUbicacionX() 
				   || r.getPuntos().get(j).getUbicacionY() != r.getPuntos().get(j+1).getUbicacionY() )) {
					
					Punto nuevoPunto = null; 
					if(r.getPuntos().get(j).getUbicacionY() -r.getPuntos().get(j+1).getUbicacionY() < - 1) {
						
						nuevoPunto = new Punto(r.getPuntos().get(j).getUbicacionX() ,r.getPuntos().get(j).getUbicacionY()+1);
						r.getPuntos().add(j+1, nuevoPunto);
						
					} else if(r.getPuntos().get(j).getUbicacionY() - r.getPuntos().get(j+1).getUbicacionY()  > 1) {
						
						nuevoPunto = new Punto(r.getPuntos().get(j).getUbicacionX() ,r.getPuntos().get(j).getUbicacionY()-1);
						r.getPuntos().add(j+1, nuevoPunto);
						
					} else if(r.getPuntos().get(j).getUbicacionX() - r.getPuntos().get(j+1).getUbicacionX()  < - 1) {
						
						nuevoPunto = new Punto(r.getPuntos().get(j).getUbicacionX()+1 ,r.getPuntos().get(j).getUbicacionY());
						r.getPuntos().add(j+1, nuevoPunto);
						
					} else if(r.getPuntos().get(j).getUbicacionX() - r.getPuntos().get(j+1).getUbicacionX() > 1) {
						
						nuevoPunto = new Punto(r.getPuntos().get(j).getUbicacionX()-1 ,r.getPuntos().get(j).getUbicacionY());
						r.getPuntos().add(j+1, nuevoPunto);
						
					}
					
					
				}
				r.getPuntos().get(j).setOrden(j+1);
			}
			i++; 
		}
		
		List<EntregaPedidoTable> entregas = new ArrayList<EntregaPedidoTable>();
		List<RecargaTable> recargas = new ArrayList<RecargaTable>();
		List<RutaTable> rutasTable = rutas.stream().map(r -> new RutaTable(r))
				.collect(Collectors.toList());
		List<PuntoTable> puntos = new ArrayList<PuntoTable>();
		rutaRepo.saveAll(rutasTable);
		for(int k=0;k<rutasTable.size();k++) {
			List<PuntoTable>puntosDeLaRuta = rutas.get(k).getPuntos().stream().map(p->new PuntoTable(p))
					.collect(Collectors.toList());	
			for(PuntoTable p : puntosDeLaRuta) {
				p.setIdRuta(rutasTable.get(k).getId());
			}
			Object o = rutas.get(k); 
			if(rutas.get(k).getTipo()==TipoRuta.ENTREGA.getValue()) {
				EntregaPedido ep = (EntregaPedido) o; 
				entregas.add(new EntregaPedidoTable(rutasTable.get(k).getId(),ep, true));
			} else if(rutas.get(k).getTipo()==TipoRuta.RECARGA.getValue()){
				Recarga r = (Recarga) o; 
				recargas.add(new RecargaTable(rutasTable.get(k).getId(),r,true));
			}
			puntos = Stream.concat(puntos.stream(), puntosDeLaRuta.stream()).collect(Collectors.toList());
		}
		puntoRepo.saveAll(puntos);
		entregaPedidoRepo.saveAll(entregas);
		recargaRepo.saveAll(recargas);
		CamionTable camion = camionRepo.findById(camionId).orElse(null);
		if(camion.getSiguienteMovimiento() == null) {
			camion.setSiguienteMovimiento(rutas.get(0).getHoraSalida());
		}
		camionRepo.save(camion);
	}


	@Override
	public ListaRutasActualesDTO listarActuales() {
		ListaRutasActualesDTO dto = new ListaRutasActualesDTO();
		List<Byte>estadosAv = new ArrayList<Byte>(); 
		estadosAv.add(EstadoCamion.AVERIADO.getValue()); 
		List<Byte>estadosOtros = new ArrayList<Byte>(); 
		estadosOtros.add(EstadoCamion.EN_RUTA.getValue());
		List<CamionRutaDTO> averiados = camionRepo.listarCamionRutaDTOByEstado(estadosAv);
		List<CamionRutaDTO> otros = camionRepo.listarCamionRutaDTOByEstado(estadosOtros);
		
		for(CamionRutaDTO crO : otros) {
			
		}
		
		dto.setAveriados(null);
		dto.setOtros(null);
		return dto;
	}

}
