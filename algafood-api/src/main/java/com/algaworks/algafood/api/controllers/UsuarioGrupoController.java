package com.algaworks.algafood.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.domain.services.CadastroUsuarioService;

@RestController
@RequestMapping("/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController {

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@GetMapping
	public ResponseEntity<List<GrupoModel>> listar(@PathVariable Long usuarioId){
		List<GrupoModel> grupos = cadastroUsuarioService.listarGrupos(usuarioId);
		return ResponseEntity.ok(grupos);
	}
	
	@PutMapping("/{grupoId}")
	public ResponseEntity<Void> associarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId){
		cadastroUsuarioService.associarGrupo(usuarioId, grupoId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{grupoId}")
	public ResponseEntity<Void> desassociarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId){
		cadastroUsuarioService.desassociarGrupo(usuarioId, grupoId);
		return ResponseEntity.noContent().build();
	}
}
