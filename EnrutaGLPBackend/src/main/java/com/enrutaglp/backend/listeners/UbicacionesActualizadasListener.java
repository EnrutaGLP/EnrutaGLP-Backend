package com.enrutaglp.backend.listeners;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.dtos.EstadoGeneralDTO;
import com.enrutaglp.backend.dtos.ListaRutasActualesDTO;
import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.events.UbicacionesActualizadasEvent;
import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.repos.interfaces.BloqueoRepository;
import com.enrutaglp.backend.repos.interfaces.RutaRepository;
import com.enrutaglp.backend.utils.Utils;

@Component
public class UbicacionesActualizadasListener {
	
	@Autowired
	private RutaRepository rutaRepository; 
	
	@Autowired
	private BloqueoRepository bloqueoRepository; 
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Value("${notificaciones.ubicaciones-actualizadas}")
	private String destino;
	
	@EventListener
	@Async
	public void onEvent(UbicacionesActualizadasEvent event) {
		List<Bloqueo>bloqueos = bloqueoRepository.listarEnRango(Utils.obtenerFechaHoraActual(),Utils.obtenerFechaHoraActual());
		ListaRutasActualesDTO rutas = rutaRepository.listarActuales();
		EstadoGeneralDTO dto = new EstadoGeneralDTO(bloqueos, rutas, null);
		template.convertAndSend(destino, dto);
	}
}
