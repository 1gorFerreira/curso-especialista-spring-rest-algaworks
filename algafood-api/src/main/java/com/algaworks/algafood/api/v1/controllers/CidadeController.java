package com.algaworks.algafood.api.v1.controllers;

import java.net.URI;
import java.util.List;

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

import com.algaworks.algafood.api.v1.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;
import com.algaworks.algafood.api.v1.openapi.controller.CidadeControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repositories.CidadeRepository;
import com.algaworks.algafood.domain.services.CadastroCidadeService;

@RestController
@RequestMapping(path = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController implements CidadeControllerOpenApi{

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;

	@Override
	@CheckSecurity.Cidades.PodeConsultar
	@GetMapping
	public CollectionModel<CidadeModel> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();
		
		return cidadeModelAssembler.toCollectionModel(todasCidades);
	}

	@Override
	@CheckSecurity.Cidades.PodeConsultar
	@GetMapping(path = "/{cidadeId}")
	public ResponseEntity<CidadeModel> buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);
		
		CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);
		
		return ResponseEntity.ok(cidadeModel);
	}

	@Override
	@CheckSecurity.Cidades.PodeEditar
	@PostMapping
	public ResponseEntity<CidadeModel> adicionar(@Valid @RequestBody CidadeInput cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
			cidade = cadastroCidadeService.salvar(cidade);
			
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(cidade.getId()).toUri();
			
			return ResponseEntity.created(uri).body(cidadeModelAssembler.toModel(cidade));
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@Override
	@CheckSecurity.Cidades.PodeEditar
	@PutMapping(path = "/{id}")
	public ResponseEntity<CidadeModel> atualizar(@PathVariable Long id, @Valid @RequestBody CidadeInput cidadeInput) {
		
		Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(id);

		cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
		
		try {	
			cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);
			return ResponseEntity.ok(cidadeModelAssembler.toModel(cidadeAtual));
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@Override
	@CheckSecurity.Cidades.PodeEditar
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
		cadastroCidadeService.excluir(cidadeId);
		return ResponseEntity.noContent().build();
	}
}
