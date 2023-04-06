package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.PermissaoModel;

public interface GrupoPermissaoControllerOpenApi {

	ResponseEntity<CollectionModel<PermissaoModel>> listar(Long grupoId);

	ResponseEntity<Void> desassociar(Long grupoId, Long permissaoId);

	ResponseEntity<Void> associar(Long grupoId, Long permissaoId);
}
