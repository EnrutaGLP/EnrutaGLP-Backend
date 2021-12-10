package com.enrutaglp.backend.scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.algorithm.Genetic;
import com.enrutaglp.backend.algorithm.Individual;
import com.enrutaglp.backend.algorithm.RutaCompleta;
import com.enrutaglp.backend.dtos.PuntoSiguienteDTO;
import com.enrutaglp.backend.enums.EstadoCamion;
import com.enrutaglp.backend.enums.ModoEjecucion;
import com.enrutaglp.backend.events.ActualizacionSimulacionEvent;
import com.enrutaglp.backend.events.SimulacionFinalizadaEvent;
import com.enrutaglp.backend.events.SimulacionIniciadaEvent;
import com.enrutaglp.backend.events.UbicacionesActualizadasEvent;
import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Camion;
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
import com.enrutaglp.backend.utils.Pair;
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
	
	@Value("${datos-configuracion.modo-ejecucion.llave}")
	private String llaveModoEjecucion;
	
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
	
	private Map<String,Future<?>> runningTasks = new HashMap<>();
	
	@Scheduled(fixedDelayString = "${algorithm.delay}")
	public void ejecutarAlgoritmoDiaADia() {
		
		Map<String, String> configuracionCompleta = configuracionRepository.listarConfiguracionCompleta();
		
		//Solo se ejecuta si se encuentra en modo dia a dia
		if(Integer.valueOf(configuracionCompleta.get(llaveModoEjecucion)) != ModoEjecucion.DIA_A_DIA.getValue()) 
			return;
		
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
		pedidos = Utils.particionarPedidos(pedidos, 15, {15});
		Map<String, Camion>flota = camionRepository.listarDisponiblesParaEnrutamiento(horaZero.format(Utils.formatter)); 
		List<Bloqueo>bloqueos = bloqueoRepository.listarEnRango(horaZero, null); 
		Map<String, List<Mantenimiento>>mantenimientos = mantenimientoRepository.obtenerMapaDeMantenimientos(horaZero,null); 
		List<Planta> plantas = new ArrayList<Planta>();
		Genetic genetic = new Genetic(pedidos, flota, bloqueos, mantenimientos,plantas, horaZero);
		
		Individual solution = genetic.run(maxIterNoImp, numChildrenToGenerate, wA, wB, wC, mu, epsilon, percentageGenesToMutate);
		
		//Si en el tiempo que se ejecuto el algoritmo se ha cambiado a modo simulacion no se registran las rutas
		configuracionCompleta = configuracionRepository.listarConfiguracionCompleta();
		if(configuracionCompleta.get(llaveModoEjecucion) != String.valueOf(ModoEjecucion.DIA_A_DIA.getValue())) 
			return;
		
		Map<String, RutaCompleta>rutasCompletas =  solution.getRutas();
		
		for(RutaCompleta rc : rutasCompletas.values()) {
			if(rc.getRutas() != null && rc.getRutas().size()>0) {
				rutaRepository.registroMasivo(rc.getCamion().getId(),rc.getRutas(),true);
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
	    
		private byte modoEjecucion; 
		private String fechaInicio; 
		private String strFechaFin; 
		private LocalDateTime fechaFin; 
		private Map<String, LocalDateTime> mapaDisponibilidad;
		private Map<String, Camion>camiones; 
		
	    public EjecucionSimulacion(byte modoEjecucion, String fechaInicio, String strFechaFin){
	    	this.modoEjecucion = modoEjecucion;
	    	this.fechaInicio = fechaInicio; 
	    	this.strFechaFin = strFechaFin; 
	    	this.fechaFin = LocalDateTime.parse(strFechaFin, Utils.formatter);
	    }
	    
	    public  Map<String, Pedido> filtrarPedidosDentroDeRango(LocalDateTime fechaFinal,  List<Pedido> pedidos){
			//Asumiendo que los pedidos se encuentran ordenados por fecha pedido
			Map<String, Pedido> pedidosMap = new HashMap<String, Pedido>(); 
			while(true) {
				if(pedidos.size() == 0)
					return pedidosMap;;
				Pedido p = pedidos.get(0);
				if(p.getFechaPedido().isAfter(fechaFinal))
					return pedidosMap; 
				pedidosMap.put(p.getCodigo(), p); 
				pedidos.remove(0); 
			}
		}
		
		public  Map<String, Camion> actualizarFlotaConMapaDisponibilidad(LocalDateTime horaZero){
			
			Map<String, Camion> flota = new HashMap<String, Camion>(); 
			
			for(Map.Entry<String, Camion> entry : camiones.entrySet()) {
				if(mapaDisponibilidad.get(entry.getKey()).isBefore(horaZero) ||
						mapaDisponibilidad.get(entry.getKey()).isEqual(horaZero)) {
					
					flota.put(entry.getKey(), entry.getValue()); 
				
				}
			}
				
			return flota;   
		}
		
		public Map<String, LocalDateTime> inicializarMapaDisponibilidad(LocalDateTime horaZero){
			Map<String, LocalDateTime> mapa = new HashMap<String, LocalDateTime>(); 
			for(Map.Entry<String, Camion> entry : camiones.entrySet()) {
				mapa.put(entry.getKey(), horaZero);
			}
			return mapa; 
		} 
	    
	    @Override
	    public void run() {
	    	
	    	
	    	Map<String, String> configuracionCompleta = configuracionRepository.listarConfiguracionCompleta();
			
			int k = Integer.valueOf(configuracionCompleta.get(llaveConstVC));
			String strUltimaHora = configuracionCompleta.get(llaveUltimoCheck);
			
			LocalDateTime nuevoCheckpoint = null;
			LocalDateTime horaZero = strUltimaHora==null? LocalDateTime.parse(fechaInicio, Utils.formatter)
					: LocalDateTime.parse(strUltimaHora, Utils.formatter);
			String fechaInicioParaNotificacion = horaZero.format(Utils.formatter); 
			if(horaZero.isEqual(fechaFin) || horaZero.isAfter(fechaFin))
				return; 
			
			int sk = saltoAlgoritmo * k;
			
			if(strUltimaHora == null) {
				//Primera ejecucion
				camiones = camionRepository.listarDisponiblesParaEnrutamiento(horaZero.format(Utils.formatter)); 
				mapaDisponibilidad = inicializarMapaDisponibilidad(horaZero);
				nuevoCheckpoint = LocalDateTime.parse(fechaInicio, Utils.formatter).plusMinutes(sk);
			}
			else {
				LocalDateTime ultimoCheckpoint = LocalDateTime.parse(strUltimaHora, Utils.formatter);
				LocalDateTime ultimoCheckpointMasSalto = ultimoCheckpoint.plusMinutes(sk); 
				if(ultimoCheckpointMasSalto.isAfter(fechaFin)) {
					nuevoCheckpoint = fechaFin;
				} else {
					nuevoCheckpoint = ultimoCheckpointMasSalto;
				}
			}
			
			String nuevoValorUltimoCheck = nuevoCheckpoint.format(Utils.formatter);
			configuracionRepository.actualizarLlave(llaveUltimoCheck, nuevoValorUltimoCheck);
			
			Map<String, Pedido> pedidosMap = pedidoRepository.listarPedidosDesdeHastaMap(fechaInicio,nuevoValorUltimoCheck);
			pedidosMap = Utils.particionarPedidos(pedidosMap, 5, 5);
			List<Pedido> pedidos = new ArrayList<>(pedidosMap.values().stream().toList());
			Collections.sort(pedidos);
			
			List<Bloqueo>bloqueos = bloqueoRepository.listarEnRango(horaZero, null); 
			Map<String, List<Mantenimiento>>mantenimientos = mantenimientoRepository.obtenerMapaDeMantenimientos(horaZero,null); 
			List<Planta> plantas = new ArrayList<Planta>();
			
			
			Map<Integer,List<Ruta>> rutas = new HashMap<Integer, List<Ruta>>();
			Map<String, Pedido> pedidosMapParaAlgoritmo; 
			Map<String, LocalDateTime> mapaDisponibilidad = new HashMap<String, LocalDateTime>();
			Map<String, Camion> flota; 
			
			while(true) {	
				if(horaZero.plusHours(1).isAfter(nuevoCheckpoint)) {
					horaZero = nuevoCheckpoint; 
				} else {
					horaZero =  horaZero.plusHours(1);
				}
				flota = actualizarFlotaConMapaDisponibilidad(horaZero);
				pedidosMapParaAlgoritmo = filtrarPedidosDentroDeRango(horaZero, pedidos);
				Genetic genetic = new Genetic(pedidosMapParaAlgoritmo, flota, bloqueos, mantenimientos,plantas, horaZero);
				Individual solution = genetic.run(maxIterNoImp, numChildrenToGenerate, wA, wB, wC, mu, epsilon, percentageGenesToMutate);
				
				Map<String, RutaCompleta>rutasCompletas =  solution.getRutas();
				
				for(RutaCompleta rc : rutasCompletas.values()) {
					if(rc.getRutas() != null && rc.getRutas().size()>0) {
						
						if(!rutas.containsKey(rc.getCamion().getId())) {
							rutas.put(rc.getCamion().getId(), new ArrayList<Ruta>());
						}
						
						List<Ruta> rs = Stream.concat(
								rutas.get(rc.getCamion().getId()).stream(),
								rc.getRutas().stream()
								).collect(Collectors.toList());
						rutas.put(rc.getCamion().getId(), rs);
						mapaDisponibilidad.put(rc.getCamion().getCodigo(), rs.get(rs.size()-1).getHoraLlegada());
					}
				}
				
				if(horaZero.isAfter(nuevoCheckpoint) || horaZero.isEqual(nuevoCheckpoint))break;
				
			}
			
			//Registrar las rutas:
			for(Map.Entry<Integer,List<Ruta>> entry : rutas.entrySet()) {
				if(entry.getValue() != null && entry.getValue().size()>0) {
					rutaRepository.registroMasivo(entry.getKey(), entry.getValue(),false);
				}
			}
			
			
			
			
			//Finalizar en el caso de que ya se haya completado la ejecucion:
			if(nuevoCheckpoint.isEqual(fechaFin) || nuevoCheckpoint.isAfter(fechaFin)) {
				//Enviar a traves de websocket:
				publisher.publishEvent(new ActualizacionSimulacionEvent(this, true, fechaInicioParaNotificacion, nuevoValorUltimoCheck, rutas));
				//Finalizar:
				publisher.publishEvent(new SimulacionFinalizadaEvent(this, modoEjecucion));
			}else {
				//Enviar a traves de websocket:
				publisher.publishEvent(new ActualizacionSimulacionEvent(this, false, fechaInicioParaNotificacion, nuevoValorUltimoCheck, rutas));
			}
	    }
	    
	}
	
	@EventListener
	@Async
	public void iniciarSimulacion(SimulacionIniciadaEvent event) throws InterruptedException {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		Runnable tarea = new EjecucionSimulacion(event.getModoEjecucion(), event.getFechaInicio(), event.getFechaFin());
		scheduler.initialize();
		if(event.getModoEjecucion() == ModoEjecucion.SIM_TRES_DIAS.getValue()) {
			runningTasks.put("3", scheduler.scheduleAtFixedRate(tarea, new Date(), 300000));
		} else if(event.getModoEjecucion() == ModoEjecucion.SIM_COLAPSO.getValue()) {
			runningTasks.put("inf", scheduler.scheduleAtFixedRate(tarea, new Date(), 300000));
		}
	}
	
	@EventListener
	public void finalizarSimulacion(SimulacionFinalizadaEvent event) {
		if(event.getModoEjecucion() == ModoEjecucion.SIM_TRES_DIAS.getValue()) {
			runningTasks.get("3").cancel(true);
		} else if(event.getModoEjecucion() == ModoEjecucion.SIM_COLAPSO.getValue()) {
			runningTasks.get("inf").cancel(true);
		}
	}
	
}
