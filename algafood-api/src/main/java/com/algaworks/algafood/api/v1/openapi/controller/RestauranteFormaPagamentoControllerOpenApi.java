package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;

@SecurityRequirement(name = "security_auth")
public interface RestauranteFormaPagamentoControllerOpenApi {

	CollectionModel<FormaPagamentoModel> listar(Long restauranteId);

	ResponseEntity<Void> removerFormaPagamento(Long restauranteId, Long formaPagamentoId);

	ResponseEntity<Void> adicionarFormaPagamento(Long restauranteId, Long formaPagamentoId);
}