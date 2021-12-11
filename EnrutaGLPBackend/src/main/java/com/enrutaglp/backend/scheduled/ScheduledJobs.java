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
import java.util.stream.Collector;
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
import com.enrutaglp.backend.enums.TipoRuta;
import com.enrutaglp.backend.events.ActualizacionSimulacionEvent;
import com.enrutaglp.backend.events.SimulacionFinalizadaEvent;
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
import com.enrutaglp.backend.repos.interfaces.EjecucionRepository;
import com.enrutaglp.backend.repos.interfaces.IndicadorRepository;
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
	private IndicadorRepository indicadorRepository; 
	
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
	
	@Autowired
	private EjecucionRepository ejecucionRepository; 
	
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
			LocalDateTime ultimoCheckpoint = LocalDateTime.parse(strUltimaHora, Utils.formatter1);
			int sk = saltoAlgoritmo * k;
			nuevoCheckpoint = ultimoCheckpoint.plusMinutes(sk);
		}
		
		String nuevoValorUltimoCheck = nuevoCheckpoint.format(Utils.formatter1);
		configuracionRepository.actualizarLlave(llaveUltimoCheck, nuevoValorUltimoCheck);
		Map<String, Pedido>pedidos = pedidoRepository.listarPendientesMap(nuevoValorUltimoCheck); 

		int[] divisores = {15};
		pedidos = Utils.particionarPedidos(pedidos, 16, divisores);
		Map<String, Camion>flota = camionRepository.listarDisponiblesParaEnrutamiento(horaZero.format(Utils.formatter1)); 
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
		LocalDateTime horaFinalizacionAlgoritmo = Utils.obtenerFechaHoraActual();
		ejecucionRepository.registrar(Integer.valueOf(configuracionCompleta.get(llaveModoEjecucion)), horaActual, horaFinalizacionAlgoritmo);
	}
	
	
	@Scheduled(fixedDelayString = "${actualizar-posiciones.delay}")
	public void actualizarUbicaciones() {
		Map<String, String> configuracionCompleta = configuracionRepository.listarConfiguracionCompleta();
		
		//Solo se ejecuta si se encuentra en modo dia a dia
		if(Integer.valueOf(configuracionCompleta.get(llaveModoEjecucion)) != ModoEjecucion.DIA_A_DIA.getValue()) 
			return;
		
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
		Map<String, Pedido> pedidosMapParaAlgoritmo;
		List<Bloqueo> bloqueosParaAlgoritmo; 
		Map<String, List<Mantenimiento>>mantenimientosParaAlgoritmo;
		private Map<String, Camion>camiones; 
		
	    public EjecucionSimulacion(byte modoEjecucion, String fechaInicio, String strFechaFin){
	    	this.modoEjecucion = modoEjecucion;
	    	this.fechaInicio = fechaInicio; 
	    	this.strFechaFin = strFechaFin; 
	    	this.fechaFin = LocalDateTime.parse(strFechaFin, Utils.formatter1);
	    }
	    
	    public void filtrarBloqueosDentroDeRango(LocalDateTime fechaFinal,LocalDateTime fechaInicialEjecucion , List<Bloqueo>bloqueos) {
	    	bloqueosParaAlgoritmo = new ArrayList<Bloqueo>(); 
	    	int i = 0; 
	    	while(true) {
	    		if(i >= bloqueos.size())
	    			break; 
	    		Bloqueo b = bloqueos.get(i); 
	    		if(b.getFechaInicio().isAfter(fechaFinal))
	    			break;
	    		
	    		if(b.getFechaFin().isBefore(fechaInicialEjecucion)) {
	    			bloqueos.remove(i); 
	    		}else {
		    		bloqueosParaAlgoritmo.add(b); 
		    		i++; 
	    		}
	    	}
	    }
	    
	    public LocalDateTime obtenerFechaLimiteMax(List<Pedido> pedidos) {
	    	LocalDateTime fechaLimiteMaximo = null;
	    	
	    	for(Pedido p: pedidos) {
	    		if(fechaLimiteMaximo == null) {
					fechaLimiteMaximo = p.getFechaLimite(); 
				} else if(fechaLimiteMaximo.isBefore(p.getFechaLimite())) {
					fechaLimiteMaximo = p.getFechaLimite(); 
				}
	    	}
	    	
	    	return fechaLimiteMaximo; 
	    }
	    
	    public LocalDateTime obtenerFechaLlegadaFinal(Map<Integer,List<Ruta>> rutas) {
	    	LocalDateTime fechaLlegadaFinal = null;
	    	
	    	for(Map.Entry<Integer,List<Ruta>> entry : rutas.entrySet()) {
	    		List<Ruta> rutasDelCamion = entry.getValue();
	    		if(rutasDelCamion != null && rutasDelCamion.size()>0) {
	    			LocalDateTime horaLlegadaUltimaRuta = rutasDelCamion.get(rutasDelCamion.size()-1).getHoraLlegada();
					if(fechaLlegadaFinal == null) {
						fechaLlegadaFinal = horaLlegadaUltimaRuta; 
					} else if(fechaLlegadaFinal.isBefore(horaLlegadaUltimaRuta)) {
						fechaLlegadaFinal = horaLlegadaUltimaRuta; 
					}
				}
			}
	    	
	    	return fechaLlegadaFinal; 
	    }
	    
	    public LocalDateTime filtrarPedidosDentroDeRango(LocalDateTime fechaFinal,  List<Pedido> pedidos){
	    	LocalDateTime fechaLimiteMaximo = null;
	    	pedidosMapParaAlgoritmo = new HashMap<String, Pedido>();
			//Asumiendo que los pedidos se encuentran ordenados por fecha pedido
			while(true) {
				if(pedidos.size() == 0)
					return fechaLimiteMaximo;
				Pedido p = pedidos.get(0);
				if(p.getFechaPedido().isAfter(fechaFinal))
					return fechaLimiteMaximo; 
				pedidosMapParaAlgoritmo.put(p.getCodigo(), p);
				if(fechaLimiteMaximo == null) {
					fechaLimiteMaximo = p.getFechaLimite(); 
				} else if(fechaLimiteMaximo.isBefore(p.getFechaLimite())) {
					fechaLimiteMaximo = p.getFechaLimite(); 
				}
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
	    	while(true) {
		    	LocalDateTime horaInicioEjecucion = Utils.obtenerFechaHoraActual();
		    	Map<String, String> configuracionCompleta = configuracionRepository.listarConfiguracionCompleta();
				
				int k = Integer.valueOf(configuracionCompleta.get(llaveConstVC));
				String strUltimaHora = configuracionCompleta.get(llaveUltimoCheck);
				
				LocalDateTime nuevoCheckpoint = null;
				LocalDateTime horaZero = strUltimaHora==null? LocalDateTime.parse(fechaInicio, Utils.formatter1)
						: LocalDateTime.parse(strUltimaHora, Utils.formatter1);
				String fechaInicioParaNotificacion = horaZero.format(Utils.formatter1); 
				if(horaZero.isEqual(fechaFin) || horaZero.isAfter(fechaFin))
					return; 
				
				int sk = saltoAlgoritmo * k;
				
				if(strUltimaHora == null) {
					//Primera ejecucion
					camiones = camionRepository.listarDisponiblesParaEnrutamiento(horaZero.format(Utils.formatter1)); 
					mapaDisponibilidad = inicializarMapaDisponibilidad(horaZero);
					nuevoCheckpoint = LocalDateTime.parse(fechaInicio, Utils.formatter1).plusMinutes(sk);
				}
				else {
					LocalDateTime ultimoCheckpoint = LocalDateTime.parse(strUltimaHora, Utils.formatter1);
					LocalDateTime ultimoCheckpointMasSalto = ultimoCheckpoint.plusMinutes(sk); 
					if(ultimoCheckpointMasSalto.isAfter(fechaFin)) {
						nuevoCheckpoint = fechaFin;
					} else {
						nuevoCheckpoint = ultimoCheckpointMasSalto;
					}
				}
				
				String nuevoValorUltimoCheck = nuevoCheckpoint.format(Utils.formatter1);
				configuracionRepository.actualizarLlave(llaveUltimoCheck, nuevoValorUltimoCheck);
				List<Bloqueo> bloqueosParaEnviar = null;
				if(strUltimaHora == null) {
					//Para obtener fechaLimiteMax de todos los pedidos y usarlo para obtener todos los bloqueos de los tres dias 
					Map<String, Pedido> pedidosTodosMap = pedidoRepository.listarPedidosDesdeHastaMap(fechaInicio,strFechaFin);
					List<Pedido> pedidosTodos = new ArrayList<>(pedidosTodosMap.values().stream().collect(Collectors.toList()));
					LocalDateTime fechaLimiteMaxGeneralTodo = obtenerFechaLimiteMax(pedidosTodos);
					bloqueosParaEnviar = bloqueoRepository.listarEnRango(horaZero, fechaLimiteMaxGeneralTodo); 
				}
				
				Map<String, Pedido> pedidosMap = pedidoRepository.listarPedidosDesdeHastaMap(fechaInicio,nuevoValorUltimoCheck);
				pedidosMap = Utils.particionarPedidos(pedidosMap, 16, new int[] {15});
				
				List<Pedido> pedidos = new ArrayList<>(pedidosMap.values().stream().collect(Collectors.toList()));
				LocalDateTime fechaLimiteMaxGeneral = obtenerFechaLimiteMax(pedidos);
				
				List<Integer> pedidosIds = pedidos.stream().map(p -> p.getId()).collect(Collectors.toList());
				Collections.sort(pedidos);
				
				List<Bloqueo>bloqueos = bloqueoRepository.listarEnRango(horaZero, fechaLimiteMaxGeneral); 
				Map<String, List<Mantenimiento>>mantenimientos = mantenimientoRepository.obtenerMapaDeMantenimientos(horaZero,fechaLimiteMaxGeneral); 
				List<Planta> plantas = new ArrayList<Planta>();
				
				
				Map<Integer,List<Ruta>> rutas = new HashMap<Integer, List<Ruta>>();
				pedidosMapParaAlgoritmo = new HashMap<String, Pedido>(); 
				Map<String, Camion> flota; 
				
				while(true) {	
					
					if(horaZero.plusHours(1).isAfter(nuevoCheckpoint)) {
						horaZero = nuevoCheckpoint; 
					} else {
						horaZero =  horaZero.plusHours(1);
					}
					flota = actualizarFlotaConMapaDisponibilidad(horaZero);
					LocalDateTime fechaLimiteMax = filtrarPedidosDentroDeRango(horaZero, pedidos);
					if(fechaLimiteMax != null) {
						filtrarBloqueosDentroDeRango(fechaLimiteMax, horaZero,bloqueos);
						Genetic genetic = new Genetic(pedidosMapParaAlgoritmo, flota, bloqueosParaAlgoritmo, mantenimientos,plantas, horaZero);
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
								
								for(Ruta r: rs) {
									if(r.getTipo() == TipoRuta.ENTREGA.getValue()) {
										Object o = r; 
										EntregaPedido ep = (EntregaPedido) o; 
										pedidosMap.remove(ep.getPedido().getCodigo());
									}
								}
							}
						}
						if(solution.getCantidadPedidosNoEntregados() > 0) {
							pedidos = new ArrayList<>(pedidosMap.values().stream().collect(Collectors.toList()));
							Collections.sort(pedidos);
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
				
				indicadorRepository.actualizarIndicadoresConPedidos(pedidosIds);
	
				LocalDateTime horaFinEjecucion = Utils.obtenerFechaHoraActual();
				ejecucionRepository.registrar(modoEjecucion, horaInicioEjecucion, horaFinEjecucion);
				
				//Finalizar en el caso de que ya se haya completado la ejecucion:
				if(nuevoCheckpoint.isEqual(fechaFin) || nuevoCheckpoint.isAfter(fechaFin)) {
					LocalDateTime fechaFinParaNotificacion = obtenerFechaLlegadaFinal(rutas); 
					String fechaFinParaNotificacionString = (fechaFinParaNotificacion != null) ? fechaFinParaNotificacion.format(Utils.formatter1) :  nuevoValorUltimoCheck; 
					//Enviar a traves de websocket:
					publisher.publishEvent(new ActualizacionSimulacionEvent(this, true, fechaInicioParaNotificacion, fechaFinParaNotificacionString, rutas));
					//Finalizar:
					publisher.publishEvent(new SimulacionFinalizadaEvent(this, modoEjecucion));
				}else {
	
					//Enviar a traves de websocket:
					if(strUltimaHora == null) {
						//En la primera ejecucion se envian los bloqueos de los 3 dias
						publisher.publishEvent(new ActualizacionSimulacionEvent(this, false, fechaInicioParaNotificacion, nuevoValorUltimoCheck, rutas,bloqueosParaEnviar));
					} else {
						publisher.publishEvent(new ActualizacionSimulacionEvent(this, false, fechaInicioParaNotificacion, nuevoValorUltimoCheck, rutas));
					}
				}
				
				if(horaZero.isAfter(fechaFin) || horaZero.isEqual(fechaFin))break;
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
