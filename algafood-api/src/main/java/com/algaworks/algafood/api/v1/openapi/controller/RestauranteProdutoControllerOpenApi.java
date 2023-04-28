package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.api.v1.model.input.ProdutoInput;

@SecurityRequirement(name = "security_auth")
public interface RestauranteProdutoControllerOpenApi {

	ResponseEntity<CollectionModel<ProdutoModel>> listarProdutos(Long restauranteId, Boolean incluirInativos);

	ResponseEntity<ProdutoModel> buscar(Long restauranteId, Long produtoId);

	ResponseEntity<ProdutoModel> adicionar(Long restauranteId, ProdutoInput produtoInput);

	ResponseEntity<ProdutoModel> atualizar(Long restauranteId, Long produtoId, ProdutoInput produtoInput);
}
