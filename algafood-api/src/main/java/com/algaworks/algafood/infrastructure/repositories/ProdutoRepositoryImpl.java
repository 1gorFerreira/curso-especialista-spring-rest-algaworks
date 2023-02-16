package com.algaworks.algafood.infrastructure.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.v1.model.FotoProduto;
import com.algaworks.algafood.domain.v1.repositories.ProdutoRepositoryQueries;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	
	@Transactional
	@Override
	public FotoProduto save(FotoProduto foto) {
		return manager.merge(foto);
	}

	@Transactional
	@Override
	public void delete(FotoProduto foto) {
		manager.remove(foto);
	}
	
}
