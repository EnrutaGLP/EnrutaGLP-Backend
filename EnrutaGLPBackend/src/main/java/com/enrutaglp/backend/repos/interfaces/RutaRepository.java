package com.enrutaglp.backend.repos.interfaces;

import java.util.List;

import com.enrutaglp.backend.dtos.ListaRutasActualesDTO;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.models.Ruta;

public interface RutaRepository {
	void eliminarTodas();
	void registroMasivo(int camionId,List<Ruta>rutas, boolean llenarPuntos);
	ListaRutasActualesDTO listarActuales();
	void actualizarRutaDespuesDeAveria(int idCamion);
}
