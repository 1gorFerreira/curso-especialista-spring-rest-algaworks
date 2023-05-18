package com.algaworks.algafood.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
