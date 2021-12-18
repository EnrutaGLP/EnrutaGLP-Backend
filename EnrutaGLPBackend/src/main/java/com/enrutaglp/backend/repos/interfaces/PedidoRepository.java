package com.enrutaglp.backend.repos.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.utils.Pair;

public interface PedidoRepository {
	List<Pedido>listarFaltantesEnLista(List<Integer> ids);
	void registrar(Pedido pedido);
	List<Pedido> listar();
	List<Pedido> listarPedidosEnRuta();
	List<Pedido> listarPedidosNoCompletados(String fechaActual);
	Map<String, Pedido> listarPendientesMap(String hasta);
	Map<String, Pedido> listarPedidosDesdeHastaMap (String desde, String hasta);
	void registrarMasivo(List<Pedido>pedidos);
}
