package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.UsuarioModel;

public interface RestauranteUsuarioResponsavelControllerOpenApi {

	ResponseEntity<CollectionModel<UsuarioModel>> listarResponsaveis(Long restauranteId);

	ResponseEntity<Void> associarResponsavel(Long restauranteId, Long usuarioId);

	ResponseEntity<Void> desassociarResponsavel(Long restauranteId, Long usuarioId);
}
