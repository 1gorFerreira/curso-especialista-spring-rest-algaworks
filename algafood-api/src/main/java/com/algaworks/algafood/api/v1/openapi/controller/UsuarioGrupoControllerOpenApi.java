package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.GrupoModel;

public interface UsuarioGrupoControllerOpenApi {

	ResponseEntity<CollectionModel<GrupoModel>> listar(Long usuarioId);

	ResponseEntity<Void> desassociarGrupo(Long usuarioId, Long grupoId);

	ResponseEntity<Void> associarGrupo(Long usuarioId, Long grupoId);
}
