package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cidades", description = "Gerencia as cidades")
public interface CidadeControllerOpenApi {

	CollectionModel<CidadeModel> listar();

	ResponseEntity<CidadeModel> buscar(Long cidadeId);

	ResponseEntity<CidadeModel> adicionar(CidadeInput cidadeInput);

	ResponseEntity<CidadeModel> atualizar(Long id, CidadeInput cidadeInput);

	ResponseEntity<?> remover(Long cidadeId);
}
