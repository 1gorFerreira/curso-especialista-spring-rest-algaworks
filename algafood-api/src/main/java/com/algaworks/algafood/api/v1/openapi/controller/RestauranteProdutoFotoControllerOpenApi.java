package com.algaworks.algafood.api.v1.openapi.controller;

import java.io.IOException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

	@Operation(summary = "Busca a foto do produto de um restaurante",
			responses = {
				@ApiResponse(responseCode = "200", content = {
						@Content(mediaType = "application/json", schema = @Schema(implementation = FotoProdutoModel.class)),
						@Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary")),
						@Content(mediaType = "image/png", schema = @Schema(type = "string", format = "binary"))
				})
	})
	ResponseEntity<FotoProdutoModel> buscar(Long restauranteId, Long produtoId);

	@Operation(hidden = true)
	ResponseEntity<?> servirFoto(Long restauranteId, Long produtoId, String acceptHeader)
			throws HttpMediaTypeNotAcceptableException;
}
