package com.enrutaglp.backend.repos.interfaces;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.enrutaglp.backend.models.Camion;

public interface CamionRepository {

	List<Camion> listar();
	Map<String,Camion> listarDisponiblesParaEnrutamiento(String horaZero);
	void actualizarMasivo(List<Camion> camiones);
	void registrar (Camion camion);
	int listarIDporCodigo (String codigoCamion);
	void actualizarEstado(int id,byte nuevoEstado);
}
