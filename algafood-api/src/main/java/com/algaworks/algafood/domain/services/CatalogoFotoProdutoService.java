package com.algaworks.algafood.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repositories.ProdutoRepository;

@Service
public class CatalogoFotoProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Transactional
	public FotoProduto salvar(FotoProduto foto) {
		return produtoRepository.save(foto);
	}
	
}
