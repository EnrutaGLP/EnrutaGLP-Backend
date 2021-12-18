package com.enrutaglp.backend.repos.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.enrutaglp.backend.dtos.HojaRutaItemDTO;
import com.enrutaglp.backend.dtos.HojaRutaItemSinPuntosDTO;
import com.enrutaglp.backend.dtos.ListaRutasActualesDTO;
import com.enrutaglp.backend.models.Mantenimiento;
import com.enrutaglp.backend.models.Ruta;

public interface RutaRepository {
	void eliminarTodas();
	List<String> registroMasivo(int camionId,List<Ruta>rutas, boolean llenarPuntos);
	ListaRutasActualesDTO listarActuales();
	void actualizarRutaDespuesDeAveria(int idCamion);
	List<HojaRutaItemDTO> listarHojaDeRuta(LocalDateTime fechaInicio);
}
