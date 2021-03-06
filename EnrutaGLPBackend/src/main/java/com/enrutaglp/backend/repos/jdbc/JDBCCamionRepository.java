package com.enrutaglp.backend.repos.jdbc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.dtos.CamionTipoDTO;
import com.enrutaglp.backend.dtos.CamionTipoDisponibilidadDTO;
import com.enrutaglp.backend.enums.EstadoCamion;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.crud.CamionCrudRepository;
import com.enrutaglp.backend.repos.interfaces.CamionRepository;
import com.enrutaglp.backend.tables.CamionTable;
import com.enrutaglp.backend.utils.Pair;

@Component
public class JDBCCamionRepository implements CamionRepository {

	@Autowired
	CamionCrudRepository repo;
	
	@Autowired
	JdbcTemplate template;

	
	
	@Override
	public List<Camion> listar() {
		List<Camion> camiones = ((List<CamionTable>)repo.findAll()).stream()
				.map(camionTable -> camionTable.toModel()).collect(Collectors.toList());
		return camiones;
	}

	@Override
	public void registrar(Camion camion) {
		
	}

	@Override
	public int listarIDporCodigo(String codigoCamion) {

		return repo.listarIDporCodigo(codigoCamion).get(0).getId();
	}

	@Override
	public Map<String, Camion> listarDisponiblesParaEnrutamiento(String horaInicial) {
		List<Camion> camiones = ((List<CamionTipoDTO>)repo.listarCamionesTipoDTODisponibles(horaInicial)).stream()
				.map(c -> c.toModel()).collect(Collectors.toList());
		Map<String,Camion> camionesMapa = new HashMap<String, Camion>();
		for(Camion c: camiones) {
			camionesMapa.put(c.getCodigo(), c);
		}
		return camionesMapa;
	}

	@Override
	public void actualizarEstado(int id, byte nuevoEstado) {
		CamionTable ct = repo.findById(id).orElse(null); 
		ct.setEstado(nuevoEstado);
		repo.save(ct);
	}

	@Override
	public void actualizarMasivo(List<Camion> camiones) {
		List<CamionTable>cts = camiones.stream().map(c -> new CamionTable(c,false)).collect(Collectors.toList()); 
		repo.saveAll(cts);
	}

	@Override
	public void resetearValoresIniciales() {
		List<CamionTable> camiones = (List<CamionTable>) repo.findAll();
		for(CamionTable ct: camiones) {
			ct.setEstado(EstadoCamion.EN_REPOSO.getValue());
			ct.setIdPuntoActual(null);
			ct.setUbicacionActualX(12);
			ct.setUbicacionActualY(8);
			ct.setSiguienteMovimiento(null);
		}
		repo.saveAll(camiones); 
		
	}

	@Override
	public Map<String, Camion> listarParaEnrutamiento() {
		List<Camion> camiones = ((List<CamionTipoDTO>)repo.listarCamionesTipoDTO()).stream()
				.map(c -> c.toModel()).collect(Collectors.toList());
		Map<String,Camion> camionesMapa = new HashMap<String, Camion>();
		for(Camion c: camiones) {
			camionesMapa.put(c.getCodigo(), c);
		}
		return camionesMapa;
	}

	@Override
	public Pair<Map<String, Camion>, Map<String, LocalDateTime>> listarParaEnrutamientoConDisponibilidad() {
		List<CamionTipoDisponibilidadDTO> camiones = (List<CamionTipoDisponibilidadDTO>)repo.listarCamionesTipoDisponibilidadDTO(); 
		Map<String,Camion> camionesMapa = new HashMap<String, Camion>();
		Map<String, LocalDateTime> disponibilidad = new HashMap<String, LocalDateTime>(); 
		for(CamionTipoDisponibilidadDTO ctd: camiones) {
			camionesMapa.put(ctd.getCodigo(), ctd.toModel());
			disponibilidad.put(ctd.getCodigo(), ctd.getHoraLlegada()); 
		}
		return new Pair<Map<String,Camion>, Map<String,LocalDateTime>>(camionesMapa, disponibilidad);
	}

	
	
}
