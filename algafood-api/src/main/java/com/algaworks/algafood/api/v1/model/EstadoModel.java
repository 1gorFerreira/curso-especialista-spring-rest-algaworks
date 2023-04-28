package com.algaworks.algafood.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "estados")
@Getter
@Setter
public class EstadoModel extends RepresentationModel<EstadoModel>{

	@Schema(example = "1")
	private Long id;

	@Schema(example = "Minas Gerais")
	private String nome;
	
}
