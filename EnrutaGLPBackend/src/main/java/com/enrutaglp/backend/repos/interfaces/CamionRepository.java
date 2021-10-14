package com.enrutaglp.backend.repos.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.enrutaglp.backend.models.Camion;

public interface CamionRepository {

	List<Camion> listar();
}
