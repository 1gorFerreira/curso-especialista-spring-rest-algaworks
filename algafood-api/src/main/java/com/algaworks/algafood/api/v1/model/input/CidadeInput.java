package com.algaworks.algafood.api.v1.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeInput {

	@Schema(example = "Uberlandia") //, required = true)
	@NotBlank
	private String nome;
	
	@Valid
//	@Schema(required = true)
	@NotNull
	private EstadoIdInput estado;
	
}
