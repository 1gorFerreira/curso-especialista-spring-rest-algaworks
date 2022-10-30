package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensível", "Mensagem incompreensível"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido");
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title){
		this.uri="https://algafood.com.br" + path;
		this.title=title;
	}
}
