package com.enrutaglp.backend.repos.interfaces;

import java.util.Date;
import java.util.List;

import com.enrutaglp.backend.models.Bloqueo;

public interface BloqueoRepository {

	void registroMasivo(List<Bloqueo>bloqueos);
	List<Bloqueo>listarEnRango(Date fechaInicio, Date fechaFin);
}
