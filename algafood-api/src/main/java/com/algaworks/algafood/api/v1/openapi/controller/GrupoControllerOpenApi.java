package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.input.GrupoInput;

public interface GrupoControllerOpenApi {

	ResponseEntity<CollectionModel<GrupoModel>> buscarTodos();

	ResponseEntity<GrupoModel> buscar(Long grupoId);

	ResponseEntity<GrupoModel> adicionar(GrupoInput grupoInput);

	ResponseEntity<GrupoModel> atualizar(Long grupoId, GrupoInput grupoInput);

	ResponseEntity<Void> deletar(Long grupoId);

}
