package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel("Problema")
@JsonInclude(Include.NON_NULL)//Só inclua na representação valores que não sejam NULL;
@Getter
@Builder
public class Problem {
	
	@ApiModelProperty(example = "400")
	private Integer status;
	
	@ApiModelProperty(example = "https://algafood.com.br/dados-invalidos")
	private String type;
	
	@ApiModelProperty(example = "Dados inválidos")
	private String title;
	
	@ApiModelProperty(example = "2023-01-18T01:26:03.1355129Z")
	private OffsetDateTime timestamp;
	
	@ApiModelProperty(value = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private String detail;
	
	@ApiModelProperty(value = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private String userMessage;
	
	@ApiModelProperty(value = "Lista de objetos ou campos que geraram o erro (opcional)")
	private List<Object> objects;
	
	@ApiModel("ObjetoProblema")
	@Getter
	@Builder
	public static class Object {
		
		@ApiModelProperty(example = "preco")
		private String name;
		
		@ApiModelProperty(example = "O preço é obrigatório")
		private String userMessage;
		
	}
}
