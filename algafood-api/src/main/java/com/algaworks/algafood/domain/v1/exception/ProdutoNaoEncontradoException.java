package com.algaworks.algafood.domain.v1.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public ProdutoNaoEncontradoException(String msg) {
		super(msg);
	}

	public ProdutoNaoEncontradoException(Long produtoId, Long restauranteId) {
		this(String.format("Não existe um cadastro de produto com código %d para o restaurante de código %d", 
				produtoId, restauranteId));
	}
}
