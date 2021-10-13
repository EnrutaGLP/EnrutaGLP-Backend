package com.enrutaglp.backend.repos.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		String sql = "INSERT INTO pedido(codigo,cliente,cantidad_glp,cantidad_glp_atendida,ubicacion_x,ubicacion_y,"
				+ "fecha_pedido,fecha_limite,estado) VALUES(?,?,?,?,?,?,?,?,?);";
		try {
			template.update(sql,pedidoTable.getCodigo(),pedidoTable.getCliente(),pedidoTable.getCantidadGlp(),pedidoTable.getCantidadGlpAtendida(),
					pedidoTable.getUbicacionX(),pedidoTable.getUbicacionY(),pedidoTable.getFechaPedido(),pedidoTable.getFechaLimite(),pedidoTable.getEstado());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
	}

	@Override
	public List<Pedido> listar() {
		List<PedidoTable> listPed = (List<PedidoTable>)repo.findAll();
		List<Pedido> pedidos = ((List<PedidoTable>)repo.findAll()).stream()
				.map(pedidoTable -> pedidoTable.toModel()).collect(Collectors.toList());
		return pedidos;
	}

}
