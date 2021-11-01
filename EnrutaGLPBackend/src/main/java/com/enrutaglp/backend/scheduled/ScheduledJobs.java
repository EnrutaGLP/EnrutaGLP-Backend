package com.enrutaglp.backend.scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.repos.interfaces.ConfiguracionRepository;
import com.enrutaglp.backend.tables.ConfiguracionTable;
@Component
public class ScheduledJobs {
	
	/*
	@Scheduled(fixedDelayString= "${helloWorld.delay}")
	public void helloWorld() {
		System.out.println("Hello world" + " now is " + new Date());
	}
	*/
	
	@Value("${datos-configuracion.const-vol-consumo.llave}")
	private String llaveConstVC;
	
	@Value("${datos-configuracion.ultimo-chk-pedidos.llave}")
	private String llaveUltimoCheck;
	
	@Value("${datos-configuracion.salto-algoritmo}")
	private int saltoAlgoritmo;
	
	@Autowired
	private ConfiguracionRepository configuracionRepository;
	
	@Scheduled(fixedDelayString = "${algorithm.delay}")
	public void executeAlgorithm() {
		Map<String, String> configuracionCompleta = configuracionRepository.listarConfiguracionCompleta();
		int k = Integer.valueOf(configuracionCompleta.get(llaveConstVC));
		String strUltimaHora = configuracionCompleta.get(llaveUltimoCheck);
		Date ultimoCheckpoint = new Date();
		if(strUltimaHora == null) {
			ultimoCheckpoint = new Date();
		}
		else {
			try {
				ultimoCheckpoint = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(strUltimaHora);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int sk = saltoAlgoritmo * k;
		long segundos = ultimoCheckpoint.getTime();
		Date nuevoCheckpoint = new Date(segundos + sk*60*1000);
		String nuevoValorUltimoCheck = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(nuevoCheckpoint);
		configuracionRepository.actualizarLlave(llaveUltimoCheck, nuevoValorUltimoCheck);
	}
	
}
