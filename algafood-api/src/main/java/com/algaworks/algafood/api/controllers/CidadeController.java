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

import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.controllers.openapi.CidadeControllerOpenApi;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repositories.CidadeRepository;
import com.algaworks.algafood.domain.services.CadastroCidadeService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cidades")
public class CidadeController implements CidadeControllerOpenApi{

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
	
	@ApiOperation("Lista as cidades")
	@GetMapping
	public List<CidadeModel> listar() {
		return cidadeModelAssembler.toCollectionModel(cidadeRepository.findAll());
	}

	@GetMapping("/{cidadeId}")
	public ResponseEntity<CidadeModel> buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);
		return ResponseEntity.ok(cidadeModelAssembler.toModel(cidade));
	}

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

	@PutMapping("/{id}")
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

	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
		cadastroCidadeService.excluir(cidadeId);
		return ResponseEntity.noContent().build();
	}
}
