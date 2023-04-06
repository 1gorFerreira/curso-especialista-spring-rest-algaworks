package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;

public interface CidadeControllerOpenApi {

	CollectionModel<CidadeModel> listar();

	ResponseEntity<CidadeModel> buscar(Long cidadeId);

	ResponseEntity<CidadeModel> adicionar(CidadeInput cidadeInput);

	ResponseEntity<CidadeModel> atualizar(Long id, CidadeInput cidadeInput);

	ResponseEntity<?> remover(Long cidadeId);
}
