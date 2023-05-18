package com.algaworks.algafood.api.v1.model.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoInput {

	@Schema(example = "1")
	@NotNull
	private Long produtoId;

	@Schema(example = "2")
	@NotNull
	@PositiveOrZero
	private Integer quantidade;

	@Schema(example = "Menos picante, por favor")
	private String observacao;
	
}
