package com.enrutaglp.backend.tables;

import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.Generated;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.enums.EstadoPedido;
import com.enrutaglp.backend.models.Camion;

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
	@Column("siguiente_movimiento")
	private LocalDateTime siguienteMovimiento;
	@Column("id_punto_actual")
	private Integer idPuntoActual; 
	private byte estado;
	private byte tipo;
	
	public CamionTable(Camion camion, boolean isNew) {
		this.codigo = camion.getCodigo(); 
		this.placa = camion.getPlaca();
		this.ubicacionActualX = camion.getUbicacionActualX();
		this.ubicacionActualY = camion.getUbicacionActualY(); 
		this.cargaActualGLP = camion.getCargaActualGLP(); 
		this.cargaActualPetroleo = camion.getCargaActualPetroleo(); 
		this.estado = camion.getEstado(); 
	}
	
	public Camion toModel() {
		return new Camion(id,codigo,placa,ubicacionActualX,ubicacionActualY,cargaActualGLP,cargaActualPetroleo,
				estado, siguienteMovimiento);
	}
}
