package com.enrutaglp.backend.repos.jdbc;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Pedido;
import com.enrutaglp.backend.repos.crud.AveriaCrudRepository;
import com.enrutaglp.backend.repos.crud.ConfiguracionCrudRepository;
import com.enrutaglp.backend.repos.crud.IndicadorCrudRepository;
import com.enrutaglp.backend.repos.interfaces.AveriaRepository;
import com.enrutaglp.backend.repos.interfaces.ConfiguracionRepository;
import com.enrutaglp.backend.repos.interfaces.IndicadorRepository;
import com.enrutaglp.backend.repos.interfaces.PedidoRepository;
import com.enrutaglp.backend.tables.AveriaTable;
import com.enrutaglp.backend.tables.ConfiguracionTable;
import com.enrutaglp.backend.tables.IndicadorTable;


@Component
public class JDBCIndicadorRepository implements IndicadorRepository{
	
	@Value("${indicadores.cantidad-pedidos-procesados.nombre}")
	private String cantidadPedidosProcesadosNombre;
	
	@Value("${indicadores.porcentaje-plazo-ocupado-promedio.nombre}")
	private String porcentajePlazoOcupadoPromedioNombre;
	
	@Autowired
	IndicadorCrudRepository repo; 
	
	@Autowired
	PedidoRepository pedidoRepository; 
	
	@Override
	public void actualizarIndicador(String nombre, Double valor) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public Map<String, Double> listarIndicadores() {
		Map<String, Double> mapaIndicadores = new HashMap<String, Double>(); 
		List<IndicadorTable>indicadoresTables = (List<IndicadorTable>) repo.findAll();
		for(IndicadorTable it : indicadoresTables) {
			mapaIndicadores.put(it.getNombre(),it.getValor());
		}
		return mapaIndicadores; 
	}


	@Override
	public void actualizarIndicadores(List<Integer> pedidosIdsActualizar) {
		
		if(pedidosIdsActualizar == null)
			return; 
		if(pedidosIdsActualizar.isEmpty())
			return;
		
		List<Pedido> pedidos = pedidoRepository.listarFaltantesEnLista(pedidosIdsActualizar); 
		Map<String, Double> mapaIndicadores = this.listarIndicadores();
		double sumaPorcentajes = 0.0; 
		int numeroPedidos = 0; 
		
		for(Pedido p: pedidos) {
			if(p.getFechaCompletado() != null) {
				Long segundosCompletado = Duration.between(p.getFechaPedido(),p.getFechaCompletado()).getSeconds(); 
				Long segundosLimite = Duration.between(p.getFechaPedido(),p.getFechaLimite()).getSeconds(); 
				double porcentajeLimiteUsado = (double)segundosCompletado*100 / segundosLimite;
				sumaPorcentajes += porcentajeLimiteUsado; 
				numeroPedidos++;
			}
		}
		Double cantidadPedidosProcesadosPasado = mapaIndicadores.get(cantidadPedidosProcesadosNombre) ;
		Double porcentajePlazoOcupadoPromedioPasado = mapaIndicadores.get(porcentajePlazoOcupadoPromedioNombre);
		Double cantidadPedidosProcesadosNuevo = cantidadPedidosProcesadosPasado + numeroPedidos; 
		Double porcentajePlazoOcupadoPromedioPedidosActuales = sumaPorcentajes/numeroPedidos;
		Double porcentajePlazoOcupadoPromedioNuevo = (porcentajePlazoOcupadoPromedioPasado*cantidadPedidosProcesadosPasado 
				+ porcentajePlazoOcupadoPromedioPedidosActuales*numeroPedidos)/cantidadPedidosProcesadosNuevo; 
		
		List<IndicadorTable>indicadoresActualizar = new ArrayList<IndicadorTable>(); 
		indicadoresActualizar.add(new IndicadorTable(cantidadPedidosProcesadosNombre,cantidadPedidosProcesadosNuevo));
		indicadoresActualizar.add(new IndicadorTable(porcentajePlazoOcupadoPromedioNombre,porcentajePlazoOcupadoPromedioNuevo));
		repo.saveAll(indicadoresActualizar);
	}
	
	
}
