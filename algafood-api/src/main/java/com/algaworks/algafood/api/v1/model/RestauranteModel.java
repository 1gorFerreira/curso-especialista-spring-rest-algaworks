package com.algaworks.algafood.api.v1.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "restaurantes")
@Setter
@Getter
public class RestauranteModel extends RepresentationModel<RestauranteModel>{
	
//	@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class}) // Marcando a propriedade dizendo de quais view faz parte;
	private Long id;
	
//	@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class})
	private String nome;
	
//	@JsonView(RestauranteView.Resumo.class)
	private BigDecimal taxaFrete;
	
//	@JsonView(RestauranteView.Resumo.class)
	private CozinhaModel cozinha;
	
	private Boolean ativo;
	private Boolean aberto;
	private EnderecoModel endereco;
	
}