package com.enrutaglp.backend.tables;

import java.util.Date;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Punto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table("punto")
@NoArgsConstructor
public class PuntoTable {
	@Id
	private int id;
	@Column("id_bloqueo")
	private Integer idBloqueo;
	private int ubicacionX; 
	private int ubicacionY;
	@Column("id_ruta")
	private int idRuta;
	private int orden; 
	
	public PuntoTable(Punto punto) {
		this.ubicacionX = punto.getUbicacionX(); 
		this.ubicacionY = punto.getUbicacionY(); 
		this.orden = punto.getOrden();
	}
	
	public Punto toModel() {
		return new Punto(id,ubicacionX,ubicacionY,orden,idBloqueo);
	}
}
