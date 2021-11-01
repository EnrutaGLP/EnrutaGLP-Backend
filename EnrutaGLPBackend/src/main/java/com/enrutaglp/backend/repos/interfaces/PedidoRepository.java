package com.enrutaglp.backend.repos.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.enrutaglp.backend.models.Pedido;

public interface PedidoRepository {
	void registrar(Pedido pedido);
	List<Pedido> listar();
	void registrarMasivo(List<Pedido>pedidos);
}
