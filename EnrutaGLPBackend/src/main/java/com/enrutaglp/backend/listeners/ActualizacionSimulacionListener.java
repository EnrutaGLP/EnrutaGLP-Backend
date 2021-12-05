package com.enrutaglp.backend.listeners;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.dtos.ActualizacionSimulacionDTO;
import com.enrutaglp.backend.dtos.CamionSimulacionDTO;
import com.enrutaglp.backend.dtos.PuntoDTO;
import com.enrutaglp.backend.dtos.RutaSimulacionDTO;
import com.enrutaglp.backend.enums.TipoRuta;
import com.enrutaglp.backend.events.ActualizacionSimulacionEvent;
import com.enrutaglp.backend.models.EntregaPedido;
import com.enrutaglp.backend.models.Ruta;

@Component
public class ActualizacionSimulacionListener {
	
	@Value("${notificaciones.actualizacion-simulacion}")
	private String destino;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@EventListener
	@Async
	public void onEvent(ActualizacionSimulacionEvent event) {
		Map<Integer,List<Ruta>> rutas = event.getRutas(); 
		List<CamionSimulacionDTO> averiados = new ArrayList<CamionSimulacionDTO>(); 
		List<CamionSimulacionDTO> otros = new ArrayList<CamionSimulacionDTO>();
		LocalDateTime horaLlegadaMayor; 
		
		//Se debe armar el objeto ActualizacionSimulacionDTO
		for(Map.Entry<Integer,List<Ruta>> entry : rutas.entrySet()) {
			if(entry.getValue() != null && entry.getValue().size()>0) {

				List<RutaSimulacionDTO> rutasParaDto = new ArrayList<RutaSimulacionDTO>();
				for(Ruta r : entry.getValue()) {
					Object o = r; 
					String codigoPedido; 
					if(r.getTipo() == TipoRuta.ENTREGA.getValue()) {
						EntregaPedido ep = (EntregaPedido) o; 
						codigoPedido = ep.getPedido().getCodigo();
					}else {
						codigoPedido = null;
					}
					List<PuntoDTO> puntos = r.getPuntos().stream().map(p -> new PuntoDTO(p))
							.collect(Collectors.toList());
					rutasParaDto.add(new RutaSimulacionDTO(codigoPedido, r.getHoraSalida(), puntos));
				}
				
				otros.add(new CamionSimulacionDTO(entry.getValue().get(0).getCamion().getCodigo(),
						rutasParaDto));
			}
		}
		//Ordenar lista 
		Collections.sort(otros);
		
		ActualizacionSimulacionDTO dto = new ActualizacionSimulacionDTO(event.getFechaInicio(), event.getFechaFin(), averiados, otros, event.isEsFinal());
		template.convertAndSend(destino, dto);
	}
	
}
