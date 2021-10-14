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
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;

@RestController
@RequestMapping("/camiones")
public class CamionController {
	
	@Autowired
	private PedidoRepository pedidoRepository; 
	
}
