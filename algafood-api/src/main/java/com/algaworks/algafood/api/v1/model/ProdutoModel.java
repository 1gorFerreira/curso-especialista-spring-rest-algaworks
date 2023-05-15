package com.algaworks.algafood.api.v1.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "produtos")
@Getter
@Setter
public class ProdutoModel extends RepresentationModel<ProdutoModel>{

	@Schema(example = "1")
	private Long id;

	@Schema(example = "Espetinho de Cupim")
	private String nome;

	@Schema(example = "Acompanha farinha, mandioca e vinagrete")
	private String descricao;

	@Schema(example = "12.50")
	private BigDecimal preco;

	@Schema(example = "true")
	private Boolean ativo;
	 
}
