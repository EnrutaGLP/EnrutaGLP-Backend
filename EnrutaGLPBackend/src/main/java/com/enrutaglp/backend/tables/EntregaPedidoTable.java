package com.enrutaglp.backend.tables;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.EntregaPedido;
import com.enrutaglp.backend.models.Punto;
import com.enrutaglp.backend.models.Ruta;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table("entrega_pedido")
@NoArgsConstructor
public class EntregaPedidoTable implements Persistable<Integer>{
	@Id
	@Column("id_ruta")
	private int idRuta;
	@Column("cantidad_entregada")
	private double cantidadEntregada; 
	@Column("id_pedido")
	private int idPedido;
	
	public EntregaPedidoTable(int idRuta,EntregaPedido ep, boolean isNew) {
		this.idRuta = idRuta;
		this.cantidadEntregada = ep.getCantidadEntregada();
		this.idPedido = ep.getPedido().getId();
		this.isNew = isNew;
	}

	@Transient
	private boolean isNew;
	

	@Override
	public boolean isNew() {
		return isNew;
	}


	@Override
	public Integer getId() {
		return idRuta;
	}
}
