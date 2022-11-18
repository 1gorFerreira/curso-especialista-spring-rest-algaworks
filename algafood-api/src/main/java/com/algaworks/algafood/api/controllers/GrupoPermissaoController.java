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

import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.services.CadastroGrupoService;

@RestController
@RequestMapping("grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@GetMapping
	public ResponseEntity<List<PermissaoModel>> listar(@PathVariable Long grupoId){
		List<PermissaoModel> permissoes = cadastroGrupoService.listarPermissoes(grupoId);
		return ResponseEntity.ok(permissoes);
	}
	
	@PutMapping("/{permissaoId}")
	public ResponseEntity<Void> asasociar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
		cadastroGrupoService.associarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{permissaoId}")
	public ResponseEntity<Void> dessasociar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
		cadastroGrupoService.desassociarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}
	
	
}
