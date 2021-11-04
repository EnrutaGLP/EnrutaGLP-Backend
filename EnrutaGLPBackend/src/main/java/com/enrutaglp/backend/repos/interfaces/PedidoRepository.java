package com.enrutaglp.backend.repos.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.enrutaglp.backend.models.Pedido;

public interface PedidoRepository {
	void registrar(Pedido pedido);
	List<Pedido> listar();
	Map<String, Pedido> listarPendientesMap();
	void registrarMasivo(List<Pedido>pedidos);
}
