package com.enrutaglp.backend.repos.jdbc;

import org.springframework.stereotype.Component;

import com.enrutaglp.backend.models.Usuario;
import com.enrutaglp.backend.repos.crud.UsuarioCrudRepository;
import com.enrutaglp.backend.repos.interfaces.UsuarioRepository;
<<<<<<< HEAD

=======
>>>>>>> c88fa24c238816f7bc2943d7fe5c5337ca04ba0f
@Component
public class JDBCUsuarioRepository implements UsuarioRepository{

	
	UsuarioCrudRepository repo;
	
	@Override
	public boolean validar (Usuario usuario) {
		
		return usuario.getCorreo().equals(repo.listarPorCorreo(usuario.getCorreo()).get(0).getCorreo());
	}
}
