package com.enrutaglp.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoRepository pedidoRepository; 
	
	@PostMapping("/registrar")
	@CrossOrigin
	public ResponseEntity<Response> registrar(@RequestBody Pedido pedido) {
		pedidoRepository.registrar(pedido);
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
	
	@GetMapping("/listar")
	@CrossOrigin
	public ResponseEntity<Response> listarTodos(){
		List<Pedido>pedidos = pedidoRepository.listar();
		return new ResponseEntity<Response>(new Response(pedidos),HttpStatus.OK);
	}
	
}
