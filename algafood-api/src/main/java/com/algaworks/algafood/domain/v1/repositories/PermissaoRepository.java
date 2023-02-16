package com.algaworks.algafood.domain.v1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.v1.model.Permissao;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long>{
	
}
