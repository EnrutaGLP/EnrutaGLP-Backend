package com.enrutaglp.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.models.Usuario;
import com.enrutaglp.backend.repos.interfaces.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@PostMapping("/validar")
	public ResponseEntity<Response> validar (@RequestBody Usuario usuario){
		
		if (usuarioRepository.validar(usuario)) {
			return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Response>(new Response(false),HttpStatus.OK);
		}
	}
}
