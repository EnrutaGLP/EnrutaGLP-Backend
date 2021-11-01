package com.enrutaglp.backend.tables;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.models.Bloqueo;

import lombok.Getter;

@Getter
@Table("bloqueo")
public class BloqueoTable {
	@Id
	private int id;
	@Column("fecha_inicio")
	private Date fechaInicio;
	@Column("fecha_fin")
	private Date fechaFin;
	
	public BloqueoTable(Bloqueo bloqueo) {
		this.fechaInicio = bloqueo.getFechaInicio(); 
		this.fechaFin = bloqueo.getFechaFin();
	}
	
}
