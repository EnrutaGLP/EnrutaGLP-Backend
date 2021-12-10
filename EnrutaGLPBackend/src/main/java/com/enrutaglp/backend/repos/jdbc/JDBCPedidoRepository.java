package com.enrutaglp.backend.repos.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.crud.PedidoCrudRepository;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;
import com.enrutaglp.backend.tables.PedidoTable;

@Component
public class JDBCPedidoRepository implements PedidoRepository {

	@Autowired
	PedidoCrudRepository repo;
	
	@Autowired
	JdbcTemplate template;

	@Override
	public void registrar(Pedido pedido) {
		PedidoTable pedidoTable = new PedidoTable(pedido, true);
		String sql = "INSERT INTO pedido(codigo,cliente,cantidad_glp,cantidad_glp_atendida,cantidad_glp_por_planificar,ubicacion_x,ubicacion_y,"
				+ "fecha_pedido,fecha_limite,estado) VALUES(?,?,?,?,?,?,?,?,?,?);";
		try {
			template.update(sql,pedidoTable.getCodigo(),pedidoTable.getCliente(),pedidoTable.getCantidadGlp(),pedidoTable.getCantidadGlpAtendida(),
					pedidoTable.getCantidadGlpPorPlanificar(),pedidoTable.getUbicacionX(),pedidoTable.getUbicacionY(),pedidoTable.getFechaPedido(),
					pedidoTable.getFechaLimite(),pedidoTable.getEstado());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public List<Pedido> listar() {
		List<Pedido> pedidos = ((List<PedidoTable>)repo.findAll()).stream()
				.map(pedidoTable -> pedidoTable.toModel()).collect(Collectors.toList());
		return pedidos;
	}

	@Override
	public void registrarMasivo(List<Pedido> pedidos) {
		List<PedidoTable>pedidosTable = pedidos.stream().map(p -> new PedidoTable(p,true))
				.collect(Collectors.toList());
		repo.saveAll(pedidosTable);
	}

	@Override
	public Map<String, Pedido> listarPendientesMap(String hasta) {
		List<Pedido> pedidos = ((List<PedidoTable>)repo.listarPendientesPlanificacion(hasta)).stream()
				.map(pedidoTable -> pedidoTable.toAlgorithmModel()).collect(Collectors.toList());
		Map<String,Pedido> pedidosMapa = new HashMap<String, Pedido>();
		for(Pedido p : pedidos) {
			pedidosMapa.put(p.getCodigo(), p);
		}
		return pedidosMapa;
	}

	@Override
	public List<Pedido> listarPedidosEnRuta() {
		List<Pedido> pedidos = ((List<PedidoTable>)repo.listarEnRuta()).stream()
				.map(pedidoTable -> pedidoTable.toModel()).collect(Collectors.toList());
		return pedidos;
	}

	@Override
	public Map<String, Pedido> listarPedidosDesdeHastaMap (String desde, String hasta) {
		List<Pedido> pedidos = ((List<PedidoTable>)repo.listarPedidosDesdeHasta(desde,hasta)).stream()
				.map(pedidoTable -> pedidoTable.toAlgorithmModel()).collect(Collectors.toList());
		Map<String,Pedido> pedidosMapa = new HashMap<String, Pedido>();
		for(Pedido p : pedidos) {
			pedidosMapa.put(p.getCodigo(), p);
		}
		return pedidosMapa;
	}

	@Override
	public List<Pedido> listarFaltantesEnLista(List<Integer> ids) {
		return repo.listarFaltantesEnLista(ids).stream().map(p -> p.toModel())
				.collect(Collectors.toList());
	}	

}
