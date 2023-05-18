package com.algaworks.algafood.api.v1.model.input;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestauranteInput {

	@Schema(example = "Thai Gourmet")
	@NotBlank
	private String nome;

	@Schema(example = "12.00")
	@NotNull
	@PositiveOrZero
	private BigDecimal taxaFrete;
	
	@Valid
//	@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)
	@NotNull
	private CozinhaIdInput cozinha;
	
	@Valid
	@NotNull
	private EnderecoInput endereco;
	
}
