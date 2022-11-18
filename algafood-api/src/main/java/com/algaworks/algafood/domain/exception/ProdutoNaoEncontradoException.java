package com.algaworks.algafood.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public ProdutoNaoEncontradoException(String msg) {
		super(msg);
	}

	public ProdutoNaoEncontradoException(Long produtoId) {
		this(String.format("Não existe um produto com código %d", produtoId));
	}
}
