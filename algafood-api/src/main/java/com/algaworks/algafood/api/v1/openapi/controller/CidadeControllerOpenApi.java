package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
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

	@Operation(summary = "Busca uma cidade por id")
	ResponseEntity<CidadeModel> buscar(Long cidadeId);

	@Operation(summary = "Cadastra uma cidade",
			description = "Cadastro de uma cidade, necessita de um estado e um nome valido")
	ResponseEntity<CidadeModel> adicionar(CidadeInput cidadeInput);

	@Operation(summary = "Atualizar uma cidade por ID")
	ResponseEntity<CidadeModel> atualizar(Long id, CidadeInput cidadeInput);

	@Operation(summary = "Excluir uma cidade por ID")
	ResponseEntity<?> remover(Long cidadeId);
}
