package com.algaworks.algafood.domain.v1.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public GrupoNaoEncontradoException(String msg) {
		super(msg);
	}

	public GrupoNaoEncontradoException(Long grupoId) {
		this(String.format("Não existe um grupo com código %d", grupoId));
	}
}
