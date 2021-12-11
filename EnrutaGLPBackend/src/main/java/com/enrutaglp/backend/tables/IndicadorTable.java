package com.enrutaglp.backend.tables;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table("indicador")
@NoArgsConstructor
@AllArgsConstructor
public class IndicadorTable {
	@Id
	private String nombre;
	private Double valor;
}
