package com.algaworks.algafood.api.v2.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.api.v2.openapi.controller.CozinhaControllerV2OpenApi;
import com.algaworks.algafood.domain.v2.services.CadastroCozinhaServiceV2;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping(path = "/v2/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaControllerV2 implements CozinhaControllerV2OpenApi{
	
	@Autowired
	private CadastroCozinhaServiceV2 cadastroCozinhaService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedModel<CozinhaModelV2>> listar(@PageableDefault(size = 10) Pageable pageable){
		PagedModel<CozinhaModelV2> cozinhas = cadastroCozinhaService.listar(pageable);
		return ResponseEntity.ok(cozinhas);
	}
	
	@GetMapping(value = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CozinhaModelV2> buscar(@PathVariable Long cozinhaId) {
		CozinhaModelV2 cozinha = cadastroCozinhaService.buscar(cozinhaId);
		return ResponseEntity.ok(cozinha);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModelV2 adicionar(@Valid @RequestBody CozinhaInputV2 cozinhaInput) {
		CozinhaModelV2 cozinha = cadastroCozinhaService.adicionar(cozinhaInput);
		return cozinha;
	}
	
	@PutMapping(value = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CozinhaModelV2> atualizar(@PathVariable Long cozinhaId, @Valid @RequestBody CozinhaInputV2 cozinhaInput){
		CozinhaModelV2 cozinha = cadastroCozinhaService.atualizar(cozinhaId, cozinhaInput);
		return ResponseEntity.ok(cozinha);
	}
	
	@DeleteMapping(value = "/{cozinhaId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinhaService.excluir(cozinhaId);
	}
	
}
