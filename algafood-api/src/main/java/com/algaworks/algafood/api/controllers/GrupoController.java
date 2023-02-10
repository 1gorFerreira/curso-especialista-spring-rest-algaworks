package com.algaworks.algafood.api.controllers;

import java.net.URI;

import javax.validation.Valid;

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

import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.api.openapi.controller.GrupoControllerOpenApi;
import com.algaworks.algafood.domain.services.CadastroGrupoService;

@RestController
@RequestMapping(path = "/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionModel<GrupoModel>> buscarTodos(){
		CollectionModel<GrupoModel> grupos = cadastroGrupoService.listarGrupos();
		return ResponseEntity.ok(grupos);
	}
	
	@GetMapping(path = "/{grupoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GrupoModel> buscar(@PathVariable Long grupoId){
		GrupoModel grupo = cadastroGrupoService.buscar(grupoId);
		return ResponseEntity.ok(grupo);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GrupoModel> adicionar(@Valid @RequestBody GrupoInput grupoInput){
		GrupoModel grupo = cadastroGrupoService.adicionar(grupoInput);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(grupo.getId()).toUri();
		return ResponseEntity.created(uri).body(grupo);
	}
	
	@PutMapping(path = "/{grupoId}", produces = MediaType.APPLICATION_JSON_VALUE)
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
