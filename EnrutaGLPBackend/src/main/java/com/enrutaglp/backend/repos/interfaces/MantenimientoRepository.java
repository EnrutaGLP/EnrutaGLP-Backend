package com.enrutaglp.backend.repos.interfaces;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Mantenimiento;

public interface MantenimientoRepository {
	
	Map<String, List<Mantenimiento>>obtenerMapaDeMantenimientos(LocalDateTime fechaInicio, LocalDateTime fechaFin);
	void registrarMantenimiento(Mantenimiento mantenimiento);
}
