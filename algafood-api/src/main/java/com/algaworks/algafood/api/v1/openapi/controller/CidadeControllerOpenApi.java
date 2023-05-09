package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cidades", description = "Gerencia as cidades")
public interface CidadeControllerOpenApi {

	@Operation(summary = "Lista as cidades")
	CollectionModel<CidadeModel> listar();

	@Operation(summary = "Busca uma cidade por id", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID da cidade invalido",
					content = @Content(schema = @Schema)) // Com isso ele passara um Schema vazio;
	})
	ResponseEntity<CidadeModel> buscar(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long cidadeId);

	@Operation(summary = "Cadastra uma cidade",
			description = "Cadastro de uma cidade, necessita de um estado e um nome valido")
	ResponseEntity<CidadeModel> adicionar(@RequestBody(description = "Representacao de uma nova cidade", required = true) CidadeInput cidadeInput);

	@Operation(summary = "Atualiza uma cidade por ID")
	ResponseEntity<CidadeModel> atualizar(@Parameter(description = "Id de uma cidade", example = "1", required = true)Long id,
										  @RequestBody(description = "Representacao de uma cidade com dados atualizados", required = true) CidadeInput cidadeInput);

	@Operation(summary = "Excluir uma cidade por ID")
	ResponseEntity<?> remover(@Parameter(description = "Id de uma cidade", example = "1", required = true)Long cidadeId);
}
