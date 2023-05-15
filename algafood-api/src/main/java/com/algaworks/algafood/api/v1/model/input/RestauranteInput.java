package com.algaworks.algafood.api.v1.model.input;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

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
