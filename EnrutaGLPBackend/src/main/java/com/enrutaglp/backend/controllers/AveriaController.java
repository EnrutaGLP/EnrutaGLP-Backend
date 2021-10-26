package com.enrutaglp.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrutaglp.backend.dtos.RegistroAveriaDTO;
import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.repos.interfaces.AveriaRepository;
import com.enrutaglp.backend.repos.interfaces.CamionRepository;


@RestController
@RequestMapping("/averias")
public class AveriaController {
	@Autowired
	private AveriaRepository averiaRepository; 
	
	@Autowired
	private CamionRepository camionRepository; 
	
	
	@PostMapping("/registrar")
	public ResponseEntity<Response> registrar(@RequestBody RegistroAveriaDTO registroAveriaDTO) {
		int idCamion = camionRepository.listarIDporCodigo(registroAveriaDTO.getCodigo());
		averiaRepository.registrar(new Averia(idCamion, registroAveriaDTO.getFecha()));
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
}
