package com.algaworks.algafood.api.v1.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.services.CadastroCozinhaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/v1/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController implements CozinhaControllerOpenApi {
	
	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;

	@CheckSecurity.Cozinhas.PodeConsultar
	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedModel<CozinhaModel>> listar(@PageableDefault(size = 10) Pageable pageable){
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities()); // Debugando as authorities do token informado;
		log.info("Listando cozihas com paginas de {} registros...", pageable.getPageSize());
		
		PagedModel<CozinhaModel> cozinhas = cadastroCozinhaService.listar(pageable);
		return ResponseEntity.ok(cozinhas);
	}

	@CheckSecurity.Cozinhas.PodeConsultar
	@Override
	@GetMapping(value = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CozinhaModel> buscar(@PathVariable Long cozinhaId) {
		CozinhaModel cozinha = cadastroCozinhaService.buscar(cozinhaId);
		return ResponseEntity.ok(cozinha);
	}
	
	@CheckSecurity.Cozinhas.PodeEditar
	@Override
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@Valid @RequestBody CozinhaInput cozinhaInput) {
		CozinhaModel cozinha = cadastroCozinhaService.adicionar(cozinhaInput);
		return cozinha;
	}
	
	@CheckSecurity.Cozinhas.PodeEditar
	@Override
	@PutMapping(value = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CozinhaModel> atualizar(@PathVariable Long cozinhaId, @Valid @RequestBody CozinhaInput cozinhaInput){
		CozinhaModel cozinha = cadastroCozinhaService.atualizar(cozinhaId, cozinhaInput);
		return ResponseEntity.ok(cozinha);
	}
	
	@CheckSecurity.Cozinhas.PodeEditar
	@Override
	@DeleteMapping(value = "/{cozinhaId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinhaService.excluir(cozinhaId);
	}
	
}
