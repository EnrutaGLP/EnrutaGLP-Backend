package com.enrutaglp.backend.tables;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
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
public class RecargaTable implements Persistable<Integer>{
	@Id
	@Column("id_ruta")
	private int idRuta;
	@Column("cantidad_recargada")
	private double cantidadRecargada; 
	@Column("id_planta")
	private Integer idPlanta;
	
	public RecargaTable(int idRuta,Recarga r, boolean isNew) {
		this.idRuta = idRuta; 
		this.cantidadRecargada = r.getCantidadRecargada();
		this.isNew = isNew;
		//Ahorita todas las recargas son a planta principal
		this.idPlanta = 1;
	}

	@Transient
	private boolean isNew;
	
	@Override
	public Integer getId() {
		return idRuta;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}
	
}
