package com.enrutaglp.backend.tables;

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
@Table("camion")
@NoArgsConstructor
public class CamionTable {
	@Id
	private int id;
	private String codigo; 
	private String placa; 
	@Column("ubicacion_actual_x")
	private int ubicacionActualX;
	@Column("ubicacion_actual_y")
	private int ubicacionActualY;
	@Column("carga_actual_glp")
	private double cargaActualGLP; 
	@Column("carga_actual_petroleo")
	private double cargaActualPetroleo; 
	private byte estado;
	private byte tipo;
	
}
