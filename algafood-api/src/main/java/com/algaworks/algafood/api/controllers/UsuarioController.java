package com.algaworks.algafood.api.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.services.CadastroUsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@GetMapping
	public ResponseEntity<List<UsuarioModel>> buscarTodos(){
		List<UsuarioModel> usuarios = cadastroUsuarioService.buscarTodos();
		return ResponseEntity.ok(usuarios);
	}
	
	@GetMapping("/{usuarioId}")
	public ResponseEntity<UsuarioModel> buscar(@PathVariable Long usuarioId){
		UsuarioModel usuario = cadastroUsuarioService.buscar(usuarioId);
		return ResponseEntity.ok(usuario);
	}
	
	@PostMapping
	public ResponseEntity<UsuarioModel> adicionar(@Valid @RequestBody UsuarioComSenhaInput usuarioComSenhaInput){
		UsuarioModel usuario = cadastroUsuarioService.adicionar(usuarioComSenhaInput);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(usuario.getId()).toUri();
		
		return ResponseEntity.created(uri).body(usuario);
	}
	
	@PutMapping("/{usuarioId}")
	public ResponseEntity<UsuarioModel> atualizar(@PathVariable Long usuarioId, @Valid @RequestBody UsuarioInput usuarioInput){
		UsuarioModel usuario = cadastroUsuarioService.atualizar(usuarioId, usuarioInput);
		return ResponseEntity.ok(usuario);
	}
	
	@DeleteMapping("/{usuarioId}")
	public ResponseEntity<Void> deletar(@PathVariable Long usuarioId){
		cadastroUsuarioService.deletar(usuarioId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{usuarioId}/senha")
	public ResponseEntity<Void> atualizarSenha(@PathVariable Long usuarioId, @Valid @RequestBody SenhaInput senhaInput) {
		cadastroUsuarioService.atualizarSenha(usuarioId, senhaInput);
		return ResponseEntity.noContent().build();
	}
}
