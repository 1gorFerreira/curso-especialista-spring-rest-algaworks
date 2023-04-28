package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.GrupoModel;

@SecurityRequirement(name = "security_auth")
public interface UsuarioGrupoControllerOpenApi {

	ResponseEntity<CollectionModel<GrupoModel>> listar(Long usuarioId);

	ResponseEntity<Void> desassociarGrupo(Long usuarioId, Long grupoId);

	ResponseEntity<Void> associarGrupo(Long usuarioId, Long grupoId);
}
