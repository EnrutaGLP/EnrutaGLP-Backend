package com.enrutaglp.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.repos.interfaces.ConfiguracionRepository;

@RestController
@RequestMapping("/configuracion")
public class ConfiguracionController {
	
	@Value("${datos-configuracion.const-vol-consumo.llave}")
	private String llaveConstVC;
	
	@Value("${datos-configuracion.const-vol-consumo.sim-tres-dias}")
	private String valorConstVCTresDias;
	
	@Value("${datos-configuracion.const-vol-consumo.sim-colapso}")
	private String valorConstVCColapso;
	
	@Value("${datos-configuracion.const-vol-consumo.dia-a-dia}")
	private String valorConstVCDiaAdia;
	
	@Autowired
	private ConfiguracionRepository configuracionRepository; 
	
	@PutMapping("/simulacion-tres-dias")
	public ResponseEntity<Response> cambiarAtresDias(){
		configuracionRepository.actualizarLlave(llaveConstVC, valorConstVCTresDias);
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
	
	@PutMapping("/simulacion-colapso-logistico")
	public ResponseEntity<Response> cambiarAcolapso(){
		configuracionRepository.actualizarLlave(llaveConstVC, valorConstVCColapso);
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
	
	@PutMapping("/dia-a-dia")
	public ResponseEntity<Response> cambiarAdiaAdia(){
		configuracionRepository.actualizarLlave(llaveConstVC, valorConstVCDiaAdia);
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
	
}
