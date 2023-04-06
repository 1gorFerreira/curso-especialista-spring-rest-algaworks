package com.algaworks.algafood.api.v1.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "formasPagamentos")
@Setter
@Getter
public class FormaPagamentoModel extends RepresentationModel<FormaPagamentoModel> {

	private Long id;

	private String descricao;    
}          