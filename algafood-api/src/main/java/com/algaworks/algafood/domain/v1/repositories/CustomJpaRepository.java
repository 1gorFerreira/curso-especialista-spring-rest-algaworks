package com.algaworks.algafood.domain.v1.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID>{
	
	Optional<T> buscarPrimeiro();
	
	void detach(T entity);
	
}
