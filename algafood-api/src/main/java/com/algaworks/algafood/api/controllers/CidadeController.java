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
import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repositories.CidadeRepository;
import com.algaworks.algafood.domain.services.CadastroCidadeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api(tags = "Cidades")
@RestController
@RequestMapping("/cidades")
public class CidadeController {

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

	@ApiOperation("Busca uma cidade por ID")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(implementation = Problem.class))),
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
	})
	@GetMapping("/{cidadeId}")
	public ResponseEntity<CidadeModel> buscar(@ApiParam("ID de uma cidade") @PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);
		return ResponseEntity.ok(cidadeModelAssembler.toModel(cidade));
	}

	@ApiOperation("Cadastra uma cidade")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Cidade cadastrada")
	})
	@PostMapping
	public ResponseEntity<CidadeModel> adicionar(@ApiParam(name = "corpo", value = "Representacao de uma nova cidade") 
			@Valid @RequestBody CidadeInput cidadeInput) {
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

	@ApiOperation("Atualiza uma cidade por ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Cidade atualizada"),
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
	})
	@PutMapping("/{id}")
	public ResponseEntity<CidadeModel> atualizar(@ApiParam(value = "ID de uma cidade") @PathVariable Long id, 
			@ApiParam(name = "corpo", value = "Representacao de uma cidade com os novos dados") @Valid @RequestBody CidadeInput cidadeInput) {
		
		Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(id);

		cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
		
		try {	
			cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);
			return ResponseEntity.ok(cidadeModelAssembler.toModel(cidadeAtual));
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@ApiOperation("Exclui uma cidade por ID")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Cidade excluída"),
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
	})
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@ApiParam("ID de uma cidade") @PathVariable Long cidadeId) {
		cadastroCidadeService.excluir(cidadeId);
		return ResponseEntity.noContent().build();
	}
}
