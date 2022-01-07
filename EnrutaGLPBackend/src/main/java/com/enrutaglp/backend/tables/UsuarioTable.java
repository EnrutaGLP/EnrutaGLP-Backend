package com.enrutaglp.backend.tables;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.enrutaglp.backend.models.Usuario;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table("usuario")
@NoArgsConstructor
public class UsuarioTable {
	
	@Id
	private int id;
	private String nombre;
	private String correo;
	@Column("id_perfil")
	private byte idPerfil;
	
	
	public Usuario toModel () {
		return new Usuario (id, nombre, correo, idPerfil);
	}
}
