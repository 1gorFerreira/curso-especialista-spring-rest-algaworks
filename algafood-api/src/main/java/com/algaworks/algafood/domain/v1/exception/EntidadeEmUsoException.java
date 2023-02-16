package com.algaworks.algafood.domain.v1.exception;

public class EntidadeEmUsoException extends NegocioException{
	private static final long serialVersionUID = 1L;
	
	public EntidadeEmUsoException(String msg) {
		super(msg);
	}
}
