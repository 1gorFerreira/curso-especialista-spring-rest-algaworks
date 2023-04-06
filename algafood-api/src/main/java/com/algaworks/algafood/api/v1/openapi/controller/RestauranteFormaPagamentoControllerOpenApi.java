package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;

public interface RestauranteFormaPagamentoControllerOpenApi {

	CollectionModel<FormaPagamentoModel> listar(Long restauranteId);

	ResponseEntity<Void> removerFormaPagamento(Long restauranteId, Long formaPagamentoId);

	ResponseEntity<Void> adicionarFormaPagamento(Long restauranteId, Long formaPagamentoId);
}