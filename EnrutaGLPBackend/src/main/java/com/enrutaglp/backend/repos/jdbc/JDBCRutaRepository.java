package com.enrutaglp.backend.repos.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.enrutaglp.backend.dtos.ListaRutasActualesDTO;
import com.enrutaglp.backend.dtos.CamionEstadoDTO;
import com.enrutaglp.backend.dtos.CamionRutaDTO;
import com.enrutaglp.backend.dtos.EntregaPedidoDTO;
import com.enrutaglp.backend.enums.EstadoCamion;
import com.enrutaglp.backend.enums.TipoRuta;
import com.enrutaglp.backend.models.EntregaPedido;
import com.enrutaglp.backend.models.Mantenimiento;
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
import com.enrutaglp.backend.repos.interfaces.ConfiguracionRepository;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;
import com.enrutaglp.backend.repos.interfaces.RutaRepository;
import com.enrutaglp.backend.tables.CamionTable;
import com.enrutaglp.backend.tables.EntregaPedidoTable;
import com.enrutaglp.backend.tables.PedidoTable;
import com.enrutaglp.backend.tables.PuntoTable;
import com.enrutaglp.backend.tables.RecargaTable;
import com.enrutaglp.backend.tables.RutaTable;
import com.enrutaglp.backend.utils.Utils;

@Component
public class JDBCRutaRepository implements RutaRepository {

	@Autowired
	PuntoCrudRepository puntoRepo;
	
	@Autowired
	RutaCrudRepository rutaRepo;

	@Autowired
	PedidoCrudRepository pedidoRepo;
	
	@Autowired
	CamionCrudRepository camionRepo;
	
	@Autowired
	EntregaPedidoCrudRepository entregaPedidoRepo;
	
	@Autowired
	RecargaCrudRepository recargaRepo;

	@Autowired
    ConfiguracionRepository configuracionRepository;
	
	@Autowired
	JdbcTemplate template;
	
	@Value("${datos-configuracion.const-vol-consumo.llave}")
	private String llaveConstVC;
	
	@Value("${plantas.principal.x}")
	private int plantaPrincipalX;
	
	@Value("${plantas.principal.y}")
	private int plantaPrincipalY;
	
	@Value("${datos-configuracion.tiempo-aparicion-mapa-averiado.sim-tres-dias}")
	private int minutosAparicionAveriaTresDias;
	
	@Value("${datos-configuracion.tiempo-aparicion-mapa-averiado.dia-a-dia}")
	private int minutosAparicionAveriaDiaADia;
	
	@Value("${datos-configuracion.const-vol-consumo.sim-tres-dias}")
	private String valorConstVCTresDias;
	
	@Value("${datos-configuracion.const-vol-consumo.sim-colapso}")
	private String valorConstVCColapso;
	
	@Value("${datos-configuracion.const-vol-consumo.dia-a-dia}")
	private String valorConstVCDiaAdia;
	
	@Override
	public void registroMasivo(int camionId,List<Ruta> rutas, boolean llenarPuntos) {
		int i = 0; 
		Integer x = rutaRepo.listarOrdenDeLaUltimaRuta(camionId);
		if(x==null) {
			x = 0;
		}
		
		//Llenar ruta con todos los puntos:
		for(Ruta r : rutas) {
			r.setOrden(x+1);
			x++;
			for(int j=0;j<r.getPuntos().size();j++) {
				
				if(llenarPuntos && j < (r.getPuntos().size()-1) && 
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
		List<CamionEstadoDTO> averiados = camionRepo.listarCamionRutaDTOByEstado(EstadoCamion.AVERIADO.getValue());
		List<CamionEstadoDTO> otros = camionRepo.listarCamionRutaDTOByEstado(EstadoCamion.EN_RUTA.getValue());
		List<CamionRutaDTO> otrosRuta = new ArrayList<CamionRutaDTO>();
		
		
		for(CamionEstadoDTO ce : otros) {
			CamionRutaDTO cr = 				new CamionRutaDTO(ce);
			cr.setRuta(rutaRepo.listarPuntosDtoRutaActualCamion(cr.getCodigo()));
			if(cr.getRuta()!=null && cr.getRuta().size()>0) {
				otrosRuta.add(cr);	
			}
		}
		
		dto.setAveriados(averiados);
		dto.setOtros(otrosRuta);
		return dto;
	}


	@Override
	public void actualizarRutaDespuesDeAveria(int idCamion) {
		RutaTable rt = rutaRepo.listarRutaActualCamion(idCamion);
		if(rt!=null) {
			/*List<EntregaPedidoDTO> eps = entregaPedidoRepo.listarEntregasPedidosFuturos(idCamion, rt.getOrden());
			List<PedidoTable> pedidos = new ArrayList<PedidoTable>();
			for(EntregaPedidoDTO ep : eps) {
				PedidoTable p = new PedidoTable(ep);
				p.setCantidadGlpPorPlanificar(p.getCantidadGlpPorPlanificar()-ep.getCantidadEntregada());
				pedidos.add(p);
			}
			pedidoRepo.saveAll(pedidos);*/
			
			String sql = "DELETE FROM ruta where"
					+ " orden >= ? and"
					+ " id_camion = ?;"; 
			try {
				template.update(sql,rt.getOrden(),idCamion);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		CamionTable ct = camionRepo.findById(idCamion).orElse(null);
		Map<String, String> configuracionCompleta = configuracionRepository.listarConfiguracionCompleta();
		String k = configuracionCompleta.get(llaveConstVC);
		if(k.equals(valorConstVCDiaAdia)) {
			ct.setSiguienteMovimiento(Utils.obtenerFechaHoraActual().plusMinutes(minutosAparicionAveriaDiaADia));
		} else if(k.equals(valorConstVCTresDias)) {
			ct.setSiguienteMovimiento(Utils.obtenerFechaHoraActual().plusMinutes(minutosAparicionAveriaTresDias));
		}
		camionRepo.save(ct);
	}

}
