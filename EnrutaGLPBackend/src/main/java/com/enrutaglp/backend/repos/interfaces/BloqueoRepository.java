package com.enrutaglp.backend.repos.interfaces;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.enrutaglp.backend.models.Bloqueo;

public interface BloqueoRepository {

	void registroMasivo(List<Bloqueo>bloqueos);
	List<Bloqueo>listarEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
