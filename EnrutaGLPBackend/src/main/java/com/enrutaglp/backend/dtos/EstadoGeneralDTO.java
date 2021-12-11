package com.enrutaglp.backend.dtos;

import java.util.List;

import com.enrutaglp.backend.models.Bloqueo;
import com.enrutaglp.backend.models.Pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EstadoGeneralDTO {
	List<Bloqueo>bloqueos; 
	ListaRutasActualesDTO rutas;
	List<Pedido>pedidos;
	double porcentajePlazoOcupadoPromedio;
}
