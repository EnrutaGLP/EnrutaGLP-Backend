package com.enrutaglp.backend.tables;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.models.Averia;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table("ejecucion")
@NoArgsConstructor
public class EjecucionTable {
	@Id
	private int id;
	@Column("modo_ejecucion")
	private int modoEjecucion; 
	@Column("fecha_inicio")
	private LocalDateTime fechaInicio;
	@Column("fecha_fin")
	private LocalDateTime fechaFin;
	
	public EjecucionTable(int modoEjecucion, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		this.modoEjecucion = modoEjecucion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
	
	
}
