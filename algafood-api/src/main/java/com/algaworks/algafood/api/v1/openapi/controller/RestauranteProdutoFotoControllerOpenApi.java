package com.algaworks.algafood.api.v1.openapi.controller;

import java.io.IOException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.algaworks.algafood.api.v1.model.FotoProdutoModel;
import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;

@SecurityRequirement(name = "security_auth")
public interface RestauranteProdutoFotoControllerOpenApi {

	ResponseEntity<FotoProdutoModel> atualizarFoto(Long restauranteId, Long produtoId,
			FotoProdutoInput fotoProdutoInput) throws IOException;

	ResponseEntity<Void> excluir(Long restauranteId, Long produtoId);

	ResponseEntity<FotoProdutoModel> buscar(Long restauranteId, Long produtoId);

	ResponseEntity<?> servirFoto(Long restauranteId, Long produtoId, String acceptHeader)
			throws HttpMediaTypeNotAcceptableException;
}
