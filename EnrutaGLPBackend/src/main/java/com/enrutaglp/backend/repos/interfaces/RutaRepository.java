package com.enrutaglp.backend.repos.interfaces;

import java.util.List;

import com.enrutaglp.backend.models.Ruta;

public interface RutaRepository {
	void registroMasivo(List<Ruta>rutas);
}
