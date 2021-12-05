package com.enrutaglp.backend.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CamionSimulacionDTO implements Comparable<CamionSimulacionDTO>{
	String codigo; 
	List<RutaSimulacionDTO> rutas;
	@Override
	public int compareTo(CamionSimulacionDTO o) {
		if(this.rutas == null || this.rutas.size()==0) {
			return -1;
		}
		else if(this.rutas.get(0).getHoraSalida().isAfter(o.getRutas().get(0).getHoraSalida())) {
			return 1; 
		} else {
			return -1;
		}
		
	}
}
