package com.enrutaglp.backend.repos.interfaces;

import java.util.List;

import com.enrutaglp.backend.dtos.PuntoSiguienteDTO;

public interface PuntoRepository {
	
	List<PuntoSiguienteDTO>conseguirPuntoSiguienteEnrutado(int idCamion);
	
}
