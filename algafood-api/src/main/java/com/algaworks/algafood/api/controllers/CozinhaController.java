package com.algaworks.algafood.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.api.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.domain.services.CadastroCozinhaService;

@RestController
@RequestMapping(path = "/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController implements CozinhaControllerOpenApi {
	
	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedModel<CozinhaModel>> listar(@PageableDefault(size = 10) Pageable pageable){
		PagedModel<CozinhaModel> cozinhas = cadastroCozinhaService.listar(pageable);
		return ResponseEntity.ok(cozinhas);
	}
	
	@GetMapping(value = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CozinhaModel> buscar(@PathVariable Long cozinhaId) {
		CozinhaModel cozinha = cadastroCozinhaService.buscar(cozinhaId);
		return ResponseEntity.ok(cozinha);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@Valid @RequestBody CozinhaInput cozinhaInput) {
		CozinhaModel cozinha = cadastroCozinhaService.adicionar(cozinhaInput);
		return cozinha;
	}
	
	@PutMapping(value = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CozinhaModel> atualizar(@PathVariable Long cozinhaId, @Valid @RequestBody CozinhaInput cozinhaInput){
		CozinhaModel cozinha = cadastroCozinhaService.atualizar(cozinhaId, cozinhaInput);
		return ResponseEntity.ok(cozinha);
	}
	
	@DeleteMapping(value = "/{cozinhaId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinhaService.excluir(cozinhaId);
	}
	
}
