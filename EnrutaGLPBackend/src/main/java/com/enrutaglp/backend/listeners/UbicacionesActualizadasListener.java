package com.enrutaglp.backend.listeners;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.dtos.EstadoGeneralDTO;
import com.enrutaglp.backend.dtos.HojaRutaItemDTO;
import com.enrutaglp.backend.dtos.ListaRutasActualesDTO;
import com.enrutaglp.backend.dtos.Response;
import com.enrutaglp.backend.events.UbicacionesActualizadasEvent;
import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.interfaces.BloqueoRepository;
import com.enrutaglp.backend.repos.interfaces.IndicadorRepository;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;
import com.enrutaglp.backend.repos.interfaces.RutaRepository;
import com.enrutaglp.backend.utils.Utils;

@Component
public class UbicacionesActualizadasListener {
	
	@Autowired
	private PedidoRepository pedidoRepository; 
	
	@Autowired
	private RutaRepository rutaRepository; 
	
	@Autowired
	private BloqueoRepository bloqueoRepository; 
	
	@Autowired
	private IndicadorRepository indicadorRepository; 
	
	@Value("${indicadores.porcentaje-plazo-ocupado-promedio.nombre}")
	private String porcentajePlazoOcupadoPromedioNombre;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Value("${notificaciones.ubicaciones-actualizadas}")
	private String destino;
	
	@EventListener
	@Async
	public void onEvent(UbicacionesActualizadasEvent event) {
		LocalDateTime fechaActual = Utils.obtenerFechaHoraActual();
		List<Bloqueo>bloqueos = bloqueoRepository.listarEnRango(fechaActual,fechaActual);
		ListaRutasActualesDTO rutas = rutaRepository.listarActuales();
		List<Pedido>pedidos = pedidoRepository.listarPedidosNoCompletados(fechaActual.format(Utils.formatter1));
		Map<String, Double>indicadoresMap =  indicadorRepository.listarIndicadores();
		List<HojaRutaItemDTO> hojaRuta = rutaRepository.listarHojaDeRuta(fechaActual);
		EstadoGeneralDTO dto = new EstadoGeneralDTO(bloqueos, rutas, pedidos, Utils.round(indicadoresMap.get(porcentajePlazoOcupadoPromedioNombre),2), 
				hojaRuta);
		template.convertAndSend(destino, dto);
	}
}
