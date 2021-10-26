package com.enrutaglp.backend.tables;

import java.util.Date;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.models.Averia;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table("averia")
@NoArgsConstructor
public class AveriaTable {
	@Id
	private int id;
	@Column("id_camion")
	private int idCamion;
	private Date fecha;
	
	
	public AveriaTable (Averia averia, boolean isNew) {
		
		this.idCamion = averia.getIdCamion();
		this.fecha = averia.getFecha();
	}
	
	public Averia toModel() {
		return new Averia (idCamion, fecha);
	}
}
