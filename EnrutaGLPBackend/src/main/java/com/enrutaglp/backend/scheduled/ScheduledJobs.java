package com.enrutaglp.backend.scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.algorithm.Genetic;
import com.enrutaglp.backend.algorithm.Individual;
import com.enrutaglp.backend.algorithm.RutaCompleta;
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
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;
import com.enrutaglp.backend.repos.interfaces.RutaRepository;
import com.enrutaglp.backend.tables.ConfiguracionTable;
@Component
public class ScheduledJobs {
	
	/*
	@Scheduled(fixedDelayString= "${helloWorld.delay}")
	public void helloWorld() {
		System.out.println("Hello world" + " now is " + new Date());
	}
	*/
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
	
	@Scheduled(fixedDelayString = "${algorithm.delay}")
	public void ejecutarAlgoritmo() {
		
		Map<String, String> configuracionCompleta = configuracionRepository.listarConfiguracionCompleta();
		int k = Integer.valueOf(configuracionCompleta.get(llaveConstVC));
		String strUltimaHora = configuracionCompleta.get(llaveUltimoCheck);
		Date nuevoCheckpoint = null;
		Date horaZero = new Date();
		if(strUltimaHora == null) {
			nuevoCheckpoint = new Date();
		}
		else {
			try {
				Date ultimoCheckpoint = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strUltimaHora);
				int sk = saltoAlgoritmo * k;
				long segundos = ultimoCheckpoint.getTime();
			    nuevoCheckpoint = new Date(segundos + sk*60*1000);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		String nuevoValorUltimoCheck = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nuevoCheckpoint);
		configuracionRepository.actualizarLlave(llaveUltimoCheck, nuevoValorUltimoCheck);
		
		Map<String, Pedido>pedidos = pedidoRepository.listarPendientesMap(nuevoValorUltimoCheck); 
		Map<String, Camion>flota = camionRepository.listarDisponiblesParaEnrutamiento(); 
		List<Bloqueo>bloqueos = bloqueoRepository.listarEnRango(horaZero, null); 
		Map<String, Mantenimiento>mantenimientos = new HashMap<String, Mantenimiento>(); 
		List<Planta> plantas = new ArrayList<Planta>();
		Genetic genetic = new Genetic(pedidos, flota, bloqueos, mantenimientos,plantas);
		
		Individual solution = genetic.run(maxIterNoImp, numChildrenToGenerate, wA, wB, wC, mu, epsilon, percentageGenesToMutate);
		
		Map<String, RutaCompleta>rutasCompletas =  solution.getRutas();
		
		/*
		for(RutaCompleta rc : rutasCompletas.values()) {
			rutaRepository.registroMasivo(rc.getRutas());
		}
		*/
		//pruebas
		/*List<Ruta>rutas = new ArrayList<Ruta>(); 
		rutas.add(new EntregaPedido(100, null, null, 0));
		EntregaPedido ep = null;
		for(Ruta r: rutas) {
			Object o = r; 
		    ep = (EntregaPedido) o;
		}
		ep.setCantidadEntregada(0);*/
	}
	
	/*
	@Scheduled(fixedDelayString = "${actualizar-posiciones.delay}")
	public void actualizarUbicaciones() {
		publisher.publishEvent(new UbicacionesActualizadasEvent(this));
	}
	*/
}
