package com.algaworks.algafood.domain.v1.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public UsuarioNaoEncontradoException(String msg) {
		super(msg);
	}

	public UsuarioNaoEncontradoException(Long usuarioId) {
		this(String.format("Não existe um cadastro de usuário com código %d", usuarioId));
	}
}
