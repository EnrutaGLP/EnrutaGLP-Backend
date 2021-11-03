package com.enrutaglp.backend.tables;

import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.Generated;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.enums.EstadoPedido;
import com.enrutaglp.backend.models.Pedido;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Table("pedido")
@NoArgsConstructor
public class PedidoTable {
	@Id
	private int id;
	private String codigo; 
	private String cliente; 
	@Column("cantidad_glp")
	private double cantidadGlp;
	@Column("cantidad_glp_atendida")
	private double cantidadGlpAtendida;	
	@Column("ubicacion_x")
	private int ubicacionX;
	@Column("ubicacion_y")
	private int ubicacionY;
	@Column("fecha_pedido")
	private LocalDateTime fechaPedido;
	@Column("fecha_limite")
	private LocalDateTime fechaLimite;
	@Column("fecha_completado")
	private LocalDateTime fechaCompletado;
	private byte estado;
	
	public PedidoTable(Pedido pedido, boolean isNew) {
		this.codigo = pedido.getCodigo(); 
		this.cliente = pedido.getCliente();
		this.cantidadGlp = pedido.getCantidadGlp();
		this.ubicacionX = pedido.getUbicacionX(); 
		this.ubicacionY = pedido.getUbicacionY(); 
		this.fechaPedido = pedido.getFechaPedido(); 
		this.fechaLimite = pedido.getFechaLimite(); 
		
		if(isNew) {
			this.cantidadGlpAtendida = 0; 
			this.estado = EstadoPedido.EN_COLA.getValue();
		}else {
			this.id = pedido.getId();
			this.cantidadGlpAtendida = pedido.getCantidadGlpAtendida(); 
			this.fechaCompletado = pedido.getFechaCompletado();
			this.estado = pedido.getEstado();
		}
		
	}
	
	public Pedido toModel() {
		return new Pedido(id,codigo,cliente,cantidadGlp,cantidadGlpAtendida,ubicacionX,ubicacionY,
				fechaPedido,fechaLimite,fechaCompletado,estado);
	}
}
