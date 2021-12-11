package com.enrutaglp.backend.controllers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.repos.interfaces.BloqueoRepository;
import com.enrutaglp.backend.utils.Utils;

@RestController
@RequestMapping("/bloqueos")
public class BloqueoController {
	
	@Autowired
	private BloqueoRepository bloqueoRepository; 

	@SuppressWarnings("unchecked")
	@GetMapping("/listar-proximos")
	public ResponseEntity<Response>listarProximos(@RequestBody Object requestBody){
		String fechaInicio = ((Map<String, String>)requestBody)
					.get("fechaInicio"); 
		List<Bloqueo>bloqueos = bloqueoRepository.listarEnRango(LocalDateTime.parse(fechaInicio, Utils.formatter1),null);
		return new ResponseEntity<Response>(new Response(bloqueos),HttpStatus.OK);
	}
	
	@PostMapping("/registro-masivo")
	public ResponseEntity<Response>registroMasivo(@RequestBody List<Bloqueo>bloqueos){
		bloqueoRepository.registroMasivo(bloqueos);
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
	
	@GetMapping("/actuales")
	public ResponseEntity<Response>listarBloqueosActuales(){
		List<Bloqueo>bloqueos = bloqueoRepository.listarEnRango(Utils.obtenerFechaHoraActual(),Utils.obtenerFechaHoraActual());
		return new ResponseEntity<Response>(new Response(bloqueos),HttpStatus.OK);
	}
	
}
