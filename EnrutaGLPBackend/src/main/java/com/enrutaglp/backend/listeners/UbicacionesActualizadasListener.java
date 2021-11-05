package com.enrutaglp.backend.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.events.UbicacionesActualizadasEvent;

@Component
public class UbicacionesActualizadasListener {
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Value("${notificaciones.ubicaciones-actualizadas}")
	private String destino;
	
	@EventListener
	@Async
	public void onEvent(UbicacionesActualizadasEvent event) {
		template.convertAndSend(destino, "hola xd");
	}
}
