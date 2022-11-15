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

import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.services.CadastroGrupoService;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@GetMapping
	public ResponseEntity<List<GrupoModel>> buscarTodos(){
		List<GrupoModel> grupos = cadastroGrupoService.buscarTodos();
		return ResponseEntity.ok(grupos);
	}
	
	@GetMapping("/{grupoId}")
	public ResponseEntity<GrupoModel> buscar(@PathVariable Long grupoId){
		GrupoModel grupo = cadastroGrupoService.buscar(grupoId);
		return ResponseEntity.ok(grupo);
	}
	
	@PostMapping
	public ResponseEntity<GrupoModel> adicionar(@Valid @RequestBody GrupoInput grupoInput){
		GrupoModel grupo = cadastroGrupoService.adicionar(grupoInput);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(grupo.getId()).toUri();
		return ResponseEntity.created(uri).body(grupo);
	}
	
	@PutMapping("/{grupoId}")
	public ResponseEntity<GrupoModel> atualizar(@PathVariable Long grupoId, @Valid @RequestBody GrupoInput grupoInput){
		GrupoModel grupo = cadastroGrupoService.atualizar(grupoId, grupoInput);
		return ResponseEntity.ok(grupo);
	}
	
	@DeleteMapping("/{grupoId}")
	public ResponseEntity<Void> deletar(@PathVariable Long grupoId){
		cadastroGrupoService.deletar(grupoId);
		return ResponseEntity.noContent().build();
	}
}
