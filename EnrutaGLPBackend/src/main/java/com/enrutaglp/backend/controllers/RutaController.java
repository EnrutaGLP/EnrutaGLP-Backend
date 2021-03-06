package com.enrutaglp.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enrutaglp.backend.dtos.HojaRutaItemDTO;
import com.enrutaglp.backend.dtos.ListaRutasActualesDTO;
import com.enrutaglp.backend.dtos.ListaRutasActualesHojaDTO;
import com.enrutaglp.backend.dtos.RegistroAveriaDTO;
import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.interfaces.AveriaRepository;
import com.enrutaglp.backend.repos.interfaces.CamionRepository;
import com.enrutaglp.backend.repos.interfaces.RutaRepository;
import com.enrutaglp.backend.utils.Utils;


@RestController
@RequestMapping("/rutas")
public class RutaController {
	@Autowired
	private RutaRepository rutaRepository; 
	
	@GetMapping("/actuales")
	@CrossOrigin
	public ResponseEntity<Response> listarActuales(){
		ListaRutasActualesDTO rutas = rutaRepository.listarActuales();
		List<HojaRutaItemDTO> hojaRuta = rutaRepository.listarHojaDeRuta(Utils.obtenerFechaHoraActual());
		return new ResponseEntity<Response>(new Response(new ListaRutasActualesHojaDTO(rutas.getAveriados(), rutas.getOtros(), hojaRuta)),HttpStatus.OK);
	}
	@PutMapping("/eliminar")
	public ResponseEntity<Response>eliminarTodas(){
		rutaRepository.eliminarTodas();
		return new ResponseEntity<Response>(new Response(true),HttpStatus.OK);
	}
}
