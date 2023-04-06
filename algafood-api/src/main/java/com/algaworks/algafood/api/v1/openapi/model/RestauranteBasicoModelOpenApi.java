package com.algaworks.algafood.api.v1.openapi.model;

import java.math.BigDecimal;

import com.algaworks.algafood.api.v1.model.CozinhaModel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestauranteBasicoModelOpenApi {

	private Long id;
	
	private String nome;
	
	private BigDecimal taxaFrete;
	
	private CozinhaModel cozinha;
	
}
