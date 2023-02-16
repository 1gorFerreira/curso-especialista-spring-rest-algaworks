package com.algaworks.algafood.domain.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.v1.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long>{
	
}
