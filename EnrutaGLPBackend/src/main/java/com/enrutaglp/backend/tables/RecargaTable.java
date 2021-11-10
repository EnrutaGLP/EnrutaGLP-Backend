package com.enrutaglp.backend.tables;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.models.Averia;
import com.enrutaglp.backend.models.Punto;
import com.enrutaglp.backend.models.Recarga;
import com.enrutaglp.backend.models.Ruta;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table("recarga")
@NoArgsConstructor
public class RecargaTable {
	@Id
	@Column("id_ruta")
	private int idRuta;
	@Column("cantidad_recargada")
	private double cantidadRecargada; 
	@Column("id_planta")
	private int idPlanta;
	
	public RecargaTable(int idRuta,Recarga r) {
		this.idRuta = idRuta; 
		this.cantidadRecargada = r.getCantidadRecargada();
	}
	
}
