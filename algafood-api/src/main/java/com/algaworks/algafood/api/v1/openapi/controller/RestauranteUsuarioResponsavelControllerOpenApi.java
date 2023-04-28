package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.UsuarioModel;

@SecurityRequirement(name = "security_auth")
public interface RestauranteUsuarioResponsavelControllerOpenApi {

	ResponseEntity<CollectionModel<UsuarioModel>> listarResponsaveis(Long restauranteId);

	ResponseEntity<Void> associarResponsavel(Long restauranteId, Long usuarioId);

	ResponseEntity<Void> desassociarResponsavel(Long restauranteId, Long usuarioId);
}
