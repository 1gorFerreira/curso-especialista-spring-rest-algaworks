package com.algaworks.algafood.api.v1.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

import com.algaworks.algafood.api.v1.assembler.EstadoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.EstadoModelAssembler;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.input.EstadoInput;
import com.algaworks.algafood.api.v1.openapi.controller.EstadoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repositories.EstadoRepository;
import com.algaworks.algafood.domain.services.CadastroEstadoService;

@RestController
@RequestMapping(path = "/v1/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController implements EstadoControllerOpenApi {

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CadastroEstadoService cadastroEstadoService;
	
	@Autowired
	private EstadoModelAssembler estadoModelAssembler;
	
	@Autowired
	private EstadoInputDisassembler estadoInputDisassembler;

	@CheckSecurity.Estados.PodeConsultar
	@Override
	@GetMapping
	public CollectionModel<EstadoModel> listar() {
		return estadoModelAssembler.toCollectionModel(estadoRepository.findAll());
	}

	@CheckSecurity.Estados.PodeConsultar
	@Override
	@GetMapping("/{estadoId}")
	public ResponseEntity<EstadoModel> buscar(@PathVariable Long estadoId) {
		Estado estado = cadastroEstadoService.buscarOuFalhar(estadoId);
		return ResponseEntity.ok(estadoModelAssembler.toModel(estado));
	}

	@CheckSecurity.Estados.PodeEditar
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@Valid @RequestBody EstadoInput estadoInput) {
		Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
		return estadoModelAssembler.toModel(cadastroEstadoService.salvar(estado));
	}

	@CheckSecurity.Estados.PodeEditar
	@Override
	@PutMapping("/{estadoId}")
	public ResponseEntity<EstadoModel> atualizar(@PathVariable Long estadoId, @Valid @RequestBody EstadoInput estadoInput) {
		Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(estadoId);

		estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);

		estadoAtual = cadastroEstadoService.salvar(estadoAtual);
		return ResponseEntity.ok(estadoModelAssembler.toModel(estadoAtual));

	}

	@CheckSecurity.Estados.PodeEditar
	@Override
	@DeleteMapping("/{estadoId}")
	public ResponseEntity<?> remover(@PathVariable Long estadoId) {
		cadastroEstadoService.excluir(estadoId);
		return ResponseEntity.noContent().build();
	}
}
