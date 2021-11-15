package com.enrutaglp.backend.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrutaglp.backend.dtos.RegistroAveriaDTO;
import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.enums.EstadoCamion;
import com.enrutaglp.backend.enums.TipoMantenimiento;
import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.repos.interfaces.AveriaRepository;
import com.enrutaglp.backend.repos.interfaces.CamionRepository;
import com.enrutaglp.backend.repos.interfaces.MantenimientoRepository;
import com.enrutaglp.backend.repos.interfaces.RutaRepository;
import com.enrutaglp.backend.utils.Utils;


@RestController
@RequestMapping("/averias")
public class AveriaController {
	@Autowired
	private AveriaRepository averiaRepository; 
	
	@Autowired
	private CamionRepository camionRepository; 
	
	@Autowired
	private MantenimientoRepository mantenimientoRepository;
	
	@Autowired
	private RutaRepository rutaRepository; 
	
	@Value("${camiones.tiempo-mantenimiento-correctivo}")
	private int horasMantenimientoCorrectivo;
	
	
	@PostMapping("/registrar")
	public ResponseEntity<Response> registrar(@RequestBody RegistroAveriaDTO registroAveriaDTO) {
		int idCamion = camionRepository.listarIDporCodigo(registroAveriaDTO.getCodigo());
		averiaRepository.registrar(new Averia(idCamion, registroAveriaDTO.getFecha()));
		camionRepository.actualizarEstado(idCamion, EstadoCamion.AVERIADO.getValue());
		LocalDateTime horaInicio = Utils.obtenerFechaHoraActual();
		LocalDateTime horaFin =  horaInicio.plusHours(horasMantenimientoCorrectivo);
		mantenimientoRepository.registrarMantenimiento(new Mantenimiento(idCamion, horaInicio, horaFin, TipoMantenimiento.CORRECTIVO.getValue()));
		rutaRepository.actualizarRutaDespuesDeAveria(idCamion);
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
}
