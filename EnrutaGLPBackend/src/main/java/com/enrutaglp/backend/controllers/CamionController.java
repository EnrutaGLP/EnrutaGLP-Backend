package com.enrutaglp.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.repos.interfaces.CamionRepository;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.Pedido;

@RestController
@RequestMapping("/camiones")
public class CamionController {
	
	@Autowired
	private CamionRepository camionRepository; 
	
	@PostMapping("/registrar")
	public ResponseEntity<Response> registrar(@RequestBody Camion camion) {
		//camionRepository.registrar(camion);
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
	
	@GetMapping("/listar")
	public ResponseEntity<Response> listarUbicaciones(){
		List<Camion>camiones = camionRepository.listar();
		return new ResponseEntity<Response>(new Response(camiones),HttpStatus.OK);
	}
}
