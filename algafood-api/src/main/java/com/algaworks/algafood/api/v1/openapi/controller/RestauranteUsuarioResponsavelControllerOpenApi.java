package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.UsuarioModel;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurantes")
public interface RestauranteUsuarioResponsavelControllerOpenApi {

	@Operation(summary = "Lista os usuários responsáveis associados a restaurante",
			responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
						content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	ResponseEntity<CollectionModel<UsuarioModel>> listarResponsaveis(@Parameter(description = "ID do restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Associação de restaurante com usuário responsável",
			responses = {
				@ApiResponse(responseCode = "204", description = "Associação realizada com sucesso"),
				@ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado",
						content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	ResponseEntity<Void> associarResponsavel(@Parameter(description = "ID do restaurante", example = "1", required = true) Long restauranteId,
											 @Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId);

	@Operation(summary = "Desassociação de restaurante com usuário responsável",
			responses = {
				@ApiResponse(responseCode = "204", description = "Desassociação realizada com sucesso"),
				@ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado",
						content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	ResponseEntity<Void> desassociarResponsavel(@Parameter(description = "ID do restaurante", example = "1", required = true) Long restauranteId,
												@Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId);
}
