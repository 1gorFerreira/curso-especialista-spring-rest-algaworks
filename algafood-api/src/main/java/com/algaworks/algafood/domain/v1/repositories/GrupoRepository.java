package com.algaworks.algafood.domain.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.v1.model.Grupo;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long>{
	
}
