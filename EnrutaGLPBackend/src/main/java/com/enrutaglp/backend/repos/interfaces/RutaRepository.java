package com.enrutaglp.backend.repos.interfaces;

import java.util.List;

import com.enrutaglp.backend.dtos.ListaRutasActualesDTO;
import com.enrutaglp.backend.models.Ruta;

public interface RutaRepository {
	void registroMasivo(int camionId,List<Ruta>rutas);
	ListaRutasActualesDTO listarActuales();
}
