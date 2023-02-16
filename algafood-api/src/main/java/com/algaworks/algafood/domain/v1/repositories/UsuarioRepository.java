package com.algaworks.algafood.domain.v1.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.v1.model.Usuario;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long>{

	Optional<Usuario> findByEmail(String email);
	
}
