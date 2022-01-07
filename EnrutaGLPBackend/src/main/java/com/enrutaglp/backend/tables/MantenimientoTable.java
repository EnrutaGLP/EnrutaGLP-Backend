package com.enrutaglp.backend.tables;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Mantenimiento;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table("mantenimiento")
@NoArgsConstructor
public class MantenimientoTable {
	@Id
	private int id;
	@Column("id_camion")
	private int idCamion;
	@Column("fecha_inicio")
	private LocalDateTime fechaInicio;
	@Column("fecha_fin")
	private LocalDateTime fechaFin;
	private byte tipo; 
	
	
	public MantenimientoTable (Mantenimiento mantenimiento, boolean isNew) {
		if(!isNew) {
			this.id = mantenimiento.getId();
		}
		this.idCamion = mantenimiento.getIdCamion(); 
		this.fechaInicio = mantenimiento.getFechaInicio(); 
		this.fechaFin = mantenimiento.getFechaFin();
		this.tipo = mantenimiento.getTipo();
	}
	
	public Mantenimiento toModel() {
		return new Mantenimiento(id,idCamion,fechaInicio,fechaFin,tipo);
	}
}
