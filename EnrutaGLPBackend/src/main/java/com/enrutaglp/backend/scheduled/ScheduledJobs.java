package com.enrutaglp.backend.scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.algorithm.Genetic;
import com.enrutaglp.backend.algorithm.Individual;
import com.enrutaglp.backend.algorithm.RutaCompleta;
import com.enrutaglp.backend.dtos.PuntoSiguienteDTO;
import com.enrutaglp.backend.enums.EstadoCamion;
import com.enrutaglp.backend.events.SimulacionIniciadaEvent;
import com.enrutaglp.backend.events.UbicacionesActualizadasEvent;
import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
import com.enrutaglp.backend.models.EntregaPedido;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.models.Planta;
import com.enrutaglp.backend.models.Ruta;
import com.enrutaglp.backend.repos.interfaces.BloqueoRepository;
import com.enrutaglp.backend.repos.interfaces.CamionRepository;
import com.enrutaglp.backend.repos.interfaces.ConfiguracionRepository;
import com.enrutaglp.backend.repos.interfaces.MantenimientoRepository;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;
import com.enrutaglp.backend.repos.interfaces.PuntoRepository;
import com.enrutaglp.backend.repos.interfaces.RutaRepository;
import com.enrutaglp.backend.tables.CamionTable;
import com.enrutaglp.backend.tables.ConfiguracionTable;
import com.enrutaglp.backend.utils.Utils;
@Component
public class ScheduledJobs {
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Value("${datos-configuracion.const-vol-consumo.llave}")
	private String llaveConstVC;
	
	@Value("${datos-configuracion.ultimo-chk-pedidos.llave}")
	private String llaveUltimoCheck;
	
	@Value("${datos-configuracion.salto-algoritmo}")
	private int saltoAlgoritmo;
	
	@Value("${algorithm.params.max-iter-no-imp}")
	private int maxIterNoImp;
	
	@Value("${algorithm.params.num-children-to-generate}")
	private int numChildrenToGenerate;
	
	@Value("${algorithm.params.wA}")
	private double wA;
	
	@Value("${algorithm.params.wB}")
	private double wB;
	
	@Value("${algorithm.params.wC}")
	private double wC;
	
	@Value("${algorithm.params.population-mu}")
	private int mu;
	
	@Value("${algorithm.params.population-epsilon}")
	private int epsilon;
	
	@Value("${algorithm.params.percent-genes-mutate}")
	private double percentageGenesToMutate;
	
	@Value("${algorithm.tiempo-aprox-ejecucion}")
	private int tiempoEjecucionAproximado;
	
	@Value("${camiones.segundos-entre-movimiento}")
	private int segundosEntreMovimiento;
	
	@Autowired
	private ConfiguracionRepository configuracionRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository; 
	
	@Autowired
	private BloqueoRepository bloqueoRepository;

	@Autowired
	private CamionRepository camionRepository;
	
	@Autowired
	private RutaRepository rutaRepository; 
	
	@Autowired
	private MantenimientoRepository mantenimientoRepository; 
	
	@Autowired
	private PuntoRepository puntoRepository; 
	

	@Scheduled(fixedDelayString = "${algorithm.delay}")
	public void ejecutarAlgoritmo() {
		
		Map<String, String> configuracionCompleta = configuracionRepository.listarConfiguracionCompleta();
		int k = Integer.valueOf(configuracionCompleta.get(llaveConstVC));
		String strUltimaHora = configuracionCompleta.get(llaveUltimoCheck);
		LocalDateTime nuevoCheckpoint = null;
		LocalDateTime horaActual = Utils.obtenerFechaHoraActual();
		LocalDateTime horaZero = horaActual.plusMinutes(tiempoEjecucionAproximado);
		
		if(strUltimaHora == null) {
			nuevoCheckpoint = horaActual;
		}
		else {
			LocalDateTime ultimoCheckpoint = LocalDateTime.parse(strUltimaHora, Utils.formatter);
			int sk = saltoAlgoritmo * k;
			nuevoCheckpoint = ultimoCheckpoint.plusMinutes(sk);
		}
		
		String nuevoValorUltimoCheck = nuevoCheckpoint.format(Utils.formatter);
		configuracionRepository.actualizarLlave(llaveUltimoCheck, nuevoValorUltimoCheck);
		Map<String, Pedido>pedidos = pedidoRepository.listarPendientesMap(nuevoValorUltimoCheck); 
		pedidos = Utils.particionarPedidos(pedidos, 5, 5);
		Map<String, Camion>flota = camionRepository.listarDisponiblesParaEnrutamiento(horaZero.format(Utils.formatter)); 
		List<Bloqueo>bloqueos = bloqueoRepository.listarEnRango(horaZero, null); 
		Map<String, List<Mantenimiento>>mantenimientos = mantenimientoRepository.obtenerMapaDeMantenimientos(horaZero,null); 
		List<Planta> plantas = new ArrayList<Planta>();
		Genetic genetic = new Genetic(pedidos, flota, bloqueos, mantenimientos,plantas, horaZero);
		
		Individual solution = genetic.run(maxIterNoImp, numChildrenToGenerate, wA, wB, wC, mu, epsilon, percentageGenesToMutate);
		
		Map<String, RutaCompleta>rutasCompletas =  solution.getRutas();
		
		for(RutaCompleta rc : rutasCompletas.values()) {
			if(rc.getRutas() != null && rc.getRutas().size()>0) {
				rutaRepository.registroMasivo(rc.getCamion().getId(),rc.getRutas());
			}
		}
	}
	
	
	@Scheduled(fixedDelayString = "${actualizar-posiciones.delay}")
	public void actualizarUbicaciones() {
		List<Camion>camiones = camionRepository.listar();
		LocalDateTime horaActual = Utils.obtenerFechaHoraActual();
		for(Camion c: camiones) {
			if(c.getSiguienteMovimiento() != null) {
				if(horaActual.isAfter(c.getSiguienteMovimiento()) || horaActual.isEqual(c.getSiguienteMovimiento())) {
					if(c.getEstado() == EstadoCamion.EN_REPOSO.getValue()) {
						c.setEstado(EstadoCamion.EN_RUTA.getValue());
					} 
					PuntoSiguienteDTO siguiente = puntoRepository.conseguirPuntoSiguienteEnrutado(c.getId());
					
					if(siguiente != null && siguiente.getId() != null) {
						c.setIdPuntoActual(siguiente.getId());
						c.setUbicacionActualX(siguiente.getUbicacionX());
						c.setUbicacionActualY(siguiente.getUbicacionY());
						if(siguiente.getSiguienteMovimiento()!=null) {
							c.setSiguienteMovimiento(siguiente.getSiguienteMovimiento());
						}else {
							c.setSiguienteMovimiento(c.getSiguienteMovimiento().plusSeconds(segundosEntreMovimiento));
						}
					}
				}
			}
		}
		
		camionRepository.actualizarMasivo(camiones);
		publisher.publishEvent(new UbicacionesActualizadasEvent(this));
	}
	
	private class EjecucionSimulacion implements Runnable{
	    
		
	    public EjecucionSimulacion(){
	    }
	    
	    @Override
	    public void run() {
	    	//Map<String, Pedido>pedidos = pedidoRepository.listarPedidosDesdeHastaMap();
	    }
	    
	}
	
	@EventListener
	@Async
	public void iniciarSimulacionTresDias(SimulacionIniciadaEvent event) {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.initialize();
		scheduler.scheduleAtFixedRate(new EjecucionSimulacion(), new Date(), 300000);
	}
	
}
