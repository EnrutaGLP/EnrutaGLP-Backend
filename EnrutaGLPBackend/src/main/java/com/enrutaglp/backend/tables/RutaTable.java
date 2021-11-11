package com.enrutaglp.backend.tables;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Punto;
import com.enrutaglp.backend.models.Ruta;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table("ruta")
@NoArgsConstructor
public class RutaTable {
	@Id
	private int id;
	@Column("consumo_petroleo")
	private double consumoPetroleo; 
	@Column("hora_llegada")
	private LocalDateTime horaLlegada;
	@Column("hora_salida")
	private LocalDateTime horaSalida;
	@Column("id_camion")
	private int idCamion;
	private byte tipo; 
	private int orden; 
	
	public RutaTable(Ruta ruta) {
		this.consumoPetroleo = ruta.getConsumoPetroleo(); 
		this.horaLlegada = ruta.getHoraLlegada(); 
		this.horaSalida = ruta.getHoraSalida(); 
		this.idCamion = ruta.getCamion().getId();
		this.orden = ruta.getOrden();
		this.tipo = ruta.getTipo();
	}
	
}
