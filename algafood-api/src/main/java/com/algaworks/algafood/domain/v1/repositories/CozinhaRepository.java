package com.algaworks.algafood.domain.v1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.v1.model.Cozinha;

@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long>{
	
	List<Cozinha> findByNomeContaining(String nome);
	
	Optional<Cozinha> findByNome(String nome);
	
	boolean existsByNome(String nome);
	
}
