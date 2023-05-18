package com.algaworks.algafood.api.v1.controllers;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.model.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.api.v1.openapi.controller.UsuarioControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.services.CadastroUsuarioService;

@RestController
@RequestMapping(path = "/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController implements UsuarioControllerOpenApi{

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping
	public ResponseEntity<CollectionModel<UsuarioModel>> buscarTodos(){
		CollectionModel<UsuarioModel> usuarios = cadastroUsuarioService.buscarTodos();
		return ResponseEntity.ok(usuarios);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@Override
	@GetMapping("/{usuarioId}")
	public ResponseEntity<UsuarioModel> buscar(@PathVariable Long usuarioId){
		UsuarioModel usuario = cadastroUsuarioService.buscar(usuarioId);
		return ResponseEntity.ok(usuario);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@Override
	@PostMapping
	public ResponseEntity<UsuarioModel> adicionar(@Valid @RequestBody UsuarioComSenhaInput usuarioComSenhaInput){
		UsuarioModel usuario = cadastroUsuarioService.adicionar(usuarioComSenhaInput);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(usuario.getId()).toUri();
		
		return ResponseEntity.created(uri).body(usuario);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeAlterarUsuario
	@Override
	@PutMapping("/{usuarioId}")
	public ResponseEntity<UsuarioModel> atualizar(@PathVariable Long usuarioId, @Valid @RequestBody UsuarioInput usuarioInput){
		UsuarioModel usuario = cadastroUsuarioService.atualizar(usuarioId, usuarioInput);
		return ResponseEntity.ok(usuario);
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@DeleteMapping("/{usuarioId}")
	public ResponseEntity<Void> deletar(@PathVariable Long usuarioId){
		cadastroUsuarioService.deletar(usuarioId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeAlterarPropriaSenha
	@Override
	@PutMapping("/{usuarioId}/senha")
	public ResponseEntity<Void> atualizarSenha(@PathVariable Long usuarioId, @Valid @RequestBody SenhaInput senhaInput) {
		cadastroUsuarioService.atualizarSenha(usuarioId, senhaInput);
		return ResponseEntity.noContent().build();
	}
}
