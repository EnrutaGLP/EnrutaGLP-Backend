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
import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.repos.interfaces.BloqueoRepository;

@RestController
@RequestMapping("/bloqueos")
public class BloqueoController {
	
	@Autowired
	private BloqueoRepository bloqueoRepository; 

	@PostMapping("/registro-masivo")
	public ResponseEntity<Response>registroMasivo(@RequestBody List<Bloqueo>bloqueos){
		bloqueoRepository.registroMasivo(bloqueos);
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
}
