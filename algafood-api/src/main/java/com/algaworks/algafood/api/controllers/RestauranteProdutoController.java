package com.algaworks.algafood.api.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.api.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.algaworks.algafood.domain.services.CadastroProdutoService;

@RestController
@RequestMapping(path = "/restaurantes/{restauranteId}/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi{

	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	
	@GetMapping
	public ResponseEntity<List<ProdutoModel>> listarProdutos(@PathVariable Long restauranteId, @RequestParam(required = false) boolean incluirInativos) {
		List<ProdutoModel> produtos = cadastroProdutoService.listarProdutos(restauranteId, incluirInativos);
		return ResponseEntity.ok(produtos);
	}
	
	@GetMapping("/{produtoId}")
	public ResponseEntity<ProdutoModel> buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId){
		ProdutoModel produto = cadastroProdutoService.buscar(restauranteId, produtoId);
		return ResponseEntity.ok(produto);
	}
	
	@PostMapping
	public ResponseEntity<ProdutoModel> adicionar(@PathVariable Long restauranteId, @Valid @RequestBody ProdutoInput produtoInput){
		ProdutoModel produto = cadastroProdutoService.adicionar(restauranteId, produtoInput);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(produto.getId()).toUri();
		
		return ResponseEntity.created(uri).body(produto);
	}
	
	@PutMapping("/{produtoId}")
	public ResponseEntity<ProdutoModel> atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid @RequestBody ProdutoInput produtoInput) {
		ProdutoModel produto = cadastroProdutoService.atualizar(restauranteId, produtoId, produtoInput);
		return ResponseEntity.ok(produto);
	}
}
