package com.enrutaglp.backend.tables;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table("configuracion")
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionTable {
	@Id
	private String llave;
	private String valor;
}
