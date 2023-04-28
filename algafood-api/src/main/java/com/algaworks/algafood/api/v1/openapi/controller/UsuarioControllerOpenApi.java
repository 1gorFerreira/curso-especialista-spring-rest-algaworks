package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.model.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;

@SecurityRequirement(name = "security_auth")
public interface UsuarioControllerOpenApi {

	ResponseEntity<CollectionModel<UsuarioModel>> buscarTodos();

	ResponseEntity<UsuarioModel> buscar(Long usuarioId);

	ResponseEntity<UsuarioModel> adicionar(UsuarioComSenhaInput usuarioInput);

	ResponseEntity<UsuarioModel> atualizar(Long usuarioId, UsuarioInput usuarioInput);

	ResponseEntity<Void> atualizarSenha(Long usuarioId, SenhaInput senha);
}