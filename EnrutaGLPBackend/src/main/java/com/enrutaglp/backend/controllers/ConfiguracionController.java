package com.enrutaglp.backend.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.enums.ModoEjecucion;
import com.enrutaglp.backend.events.SimulacionIniciadaEvent;
import com.enrutaglp.backend.events.UbicacionesActualizadasEvent;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.repos.interfaces.ConfiguracionRepository;
import com.enrutaglp.backend.utils.Utils;
import com.fasterxml.jackson.annotation.JsonFormat;

@RestController
@RequestMapping("/configuracion")
public class ConfiguracionController {
	
	@Value("${datos-configuracion.const-vol-consumo.llave}")
	private String llaveConstVC;
	
	@Value("${datos-configuracion.ultimo-chk-pedidos.llave}")
	private String llaveUltimoCheck;
	
	@Value("${datos-configuracion.modo-ejecucion.llave}")
	private String llaveModoEjecucion;
	
	@Value("${datos-configuracion.fecha-fin-simulacion.llave}")
	private String llaveFechaFinSimulacion;
	
	@Value("${datos-configuracion.const-vol-consumo.sim-tres-dias}")
	private String valorConstVCTresDias;
	
	@Value("${datos-configuracion.const-vol-consumo.sim-colapso}")
	private String valorConstVCColapso;
	
	@Value("${datos-configuracion.const-vol-consumo.dia-a-dia}")
	private String valorConstVCDiaAdia;
	
	@Autowired
	private ConfiguracionRepository configuracionRepository; 

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@SuppressWarnings("unchecked")
	@PutMapping("/simulacion-tres-dias")
	public ResponseEntity<Response> cambiarAtresDias(@RequestBody Object requestBody){
		Map<String, String> mapa = new HashMap<String, String>();
		
		mapa.put(llaveConstVC, valorConstVCTresDias); 
		mapa.put(llaveUltimoCheck, ((Map<String, String>)requestBody).get("fechaInicio")); 
		mapa.put(llaveModoEjecucion, ModoEjecucion.SIM_TRES_DIAS.toString()); 
		mapa.put(llaveFechaFinSimulacion, LocalDateTime.parse(((Map<String, String>)requestBody)
				.get("fechaInicio"), Utils.formatter).plusDays(3).format(Utils.formatter)); 
		
		configuracionRepository.actualizarLlaves(mapa);
		
		publisher.publishEvent(new SimulacionIniciadaEvent(this,ModoEjecucion.SIM_TRES_DIAS.getValue()));
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
