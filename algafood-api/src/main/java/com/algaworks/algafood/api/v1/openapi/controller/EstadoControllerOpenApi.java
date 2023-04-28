package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.input.EstadoInput;

@SecurityRequirement(name = "security_auth")
public interface EstadoControllerOpenApi {

	CollectionModel<EstadoModel> listar();

	ResponseEntity<EstadoModel> buscar(Long estadoId);

	EstadoModel adicionar(EstadoInput estadoInput);

	ResponseEntity<EstadoModel> atualizar(Long estadoId, EstadoInput estadoInput);

	ResponseEntity<?> remover(Long estadoId);
}
