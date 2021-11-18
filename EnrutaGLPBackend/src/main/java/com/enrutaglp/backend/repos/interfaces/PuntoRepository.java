package com.enrutaglp.backend.repos.interfaces;

import java.util.List;

import com.enrutaglp.backend.dtos.PuntoSiguienteDTO;

public interface PuntoRepository {
	
	PuntoSiguienteDTO conseguirPuntoSiguienteEnrutado(int idCamion);
	
}
