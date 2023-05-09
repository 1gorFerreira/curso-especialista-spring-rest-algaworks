package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)//Só inclua na representação valores que não sejam NULL;
@Getter
@Builder
@Schema(name = "Problema")
public class Problem {

	@Schema(example = "400")
	private Integer status;

	@Schema(example = "https://algafood.com.br/dados-invalidos")
	private String type;

	@Schema(example = "Dados invalidos")
	private String title;

	@Schema(example = "2022-07-15T11:21:50.902245498Z")
	private OffsetDateTime timestamp;

	@Schema(example = "Um ou mais campos estao invalidos. Faca o preenchimento correto e tente novamente")
	private String detail;

	@Schema(example = "Um ou mais campos estao invalidos. Faca o preenchimento correto e tente novamente")
	private String userMessage;

	@Schema(description = "Lista de objetos ou campos que geraram o erro")
	private List<Object> objects;
	
	@Getter
	@Builder
	@Schema(name = "ObjetoProblema")
	public static class Object {

		@Schema(example = "preco")
		private String name;

		@Schema(example = "O preco e invalido")
		private String userMessage;
		
	}
}
