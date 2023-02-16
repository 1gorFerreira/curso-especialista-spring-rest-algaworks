package com.algaworks.algafood.domain.v1.repositories;

import com.algaworks.algafood.domain.v1.model.FotoProduto;

public interface ProdutoRepositoryQueries {

	FotoProduto save(FotoProduto foto);
	
	void delete(FotoProduto foto);
	
}
