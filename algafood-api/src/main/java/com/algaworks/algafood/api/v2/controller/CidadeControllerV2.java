package com.algaworks.algafood.api.v2.controller;

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

import com.algaworks.algafood.api.v2.assembler.CidadeInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CidadeModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repositories.CidadeRepository;
import com.algaworks.algafood.domain.services.CadastroCidadeService;

@RestController
@RequestMapping(path = "/v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeControllerV2 {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@Autowired
	private CidadeModelAssemblerV2 cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassemblerV2 cidadeInputDisassembler;
	
	@GetMapping
	public CollectionModel<CidadeModelV2> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();
		
		return cidadeModelAssembler.toCollectionModel(todasCidades);
	}

	@GetMapping(path = "/{cidadeId}")
	public ResponseEntity<CidadeModelV2> buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);
		
		CidadeModelV2 cidadeModel = cidadeModelAssembler.toModel(cidade);
		
		return ResponseEntity.ok(cidadeModel);
	}

	@PostMapping
	public ResponseEntity<CidadeModelV2> adicionar(@Valid @RequestBody CidadeInputV2 cidadeInput) {
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

	@PutMapping(path = "/{id}")
	public ResponseEntity<CidadeModelV2> atualizar(@PathVariable Long id, @Valid @RequestBody CidadeInputV2 cidadeInput) {
		
		Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(id);

		cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
		
		try {	
			cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);
			return ResponseEntity.ok(cidadeModelAssembler.toModel(cidadeAtual));
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
		cadastroCidadeService.excluir(cidadeId);
		return ResponseEntity.noContent().build();
	}
}
