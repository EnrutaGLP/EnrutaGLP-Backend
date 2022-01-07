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
@Setter
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
	private String color; 
	private byte estado;
	private byte tipo;
	
	public CamionTable(Camion camion, boolean isNew) {
		if(!isNew) {
			this.id = camion.getId();
		}
		this.codigo = camion.getCodigo(); 
		this.placa = camion.getPlaca();
		this.ubicacionActualX = camion.getUbicacionActualX();
		this.ubicacionActualY = camion.getUbicacionActualY(); 
		this.cargaActualGLP = camion.getCargaActualGLP(); 
		this.cargaActualPetroleo = camion.getCargaActualPetroleo(); 
		this.siguienteMovimiento = camion.getSiguienteMovimiento(); 
		this.idPuntoActual = camion.getIdPuntoActual();
		this.estado = camion.getEstado(); 
		this.color = camion.getColor();
		this.tipo = camion.getTipoByte();
	}
	
	public Camion toModel() {
		return new Camion( id,  codigo,  placa,  ubicacionActualX,  ubicacionActualY,
				 idPuntoActual,  cargaActualGLP,  cargaActualPetroleo,  estado,  tipo,
				 siguienteMovimiento,color);
	}
}
