package com.enrutaglp.backend.repos.jdbc;

import org.springframework.stereotype.Component;

import com.enrutaglp.backend.models.Usuario;
import com.enrutaglp.backend.repos.crud.UsuarioCrudRepository;
import com.enrutaglp.backend.repos.interfaces.UsuarioRepository;
@Component
public class JDBCUsuarioRepository implements UsuarioRepository{

	
	UsuarioCrudRepository repo;
	
	@Override
	public boolean validar (Usuario usuario) {
		
		return usuario.getCorreo().equals(repo.listarPorCorreo(usuario.getCorreo()).get(0).getCorreo());
	}
}
