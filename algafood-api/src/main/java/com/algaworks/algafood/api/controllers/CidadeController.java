package com.algaworks.algafood.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repositories.CidadeRepository;
import com.algaworks.algafood.domain.services.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@GetMapping
	public List<Cidade> listar() {
		return cidadeRepository.listar();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long id){
		Cidade cidade = cidadeRepository.buscar(id);
		
		if (cidade != null) {
			return ResponseEntity.ok(cidade);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Cidade cidade){
		try {
			cidade = cadastroCidadeService.salvar(cidade);
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Cidade> atualizar(@PathVariable Long id, @RequestBody Cidade cidade) {
		Cidade cidadeAtual = cidadeRepository.buscar(id);
		
		if (cidadeAtual != null) {
//			BeanUtils.copyProperties(cidade, cidadeAtual, "id");
			cidadeAtual.setNome(cidade.getNome());
			cidadeAtual.setEstado(cidade.getEstado());
			
			cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);
			return ResponseEntity.ok(cidadeAtual);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
		try {
			cadastroCidadeService.excluir(cidadeId);	
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}
}
