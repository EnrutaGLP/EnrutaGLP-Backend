package com.enrutaglp.backend.repos.interfaces;

import java.time.LocalDateTime;


public interface EjecucionRepository {
	void registrar(int modoEjecucion, LocalDateTime fechaInicio, LocalDateTime fechaFin); 
}
