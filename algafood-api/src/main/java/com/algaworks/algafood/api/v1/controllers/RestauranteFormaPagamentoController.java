package com.algaworks.algafood.api.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.services.CadastroRestauranteService;

@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteFormaPagamentoController implements RestauranteFormaPagamentoControllerOpenApi {

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		CollectionModel<FormaPagamentoModel> formasPagamentoModel = 
				formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento())
				.removeLinks()
				.add(algaLinks.linkToRestauranteFormasPagamento(restauranteId))
				.add(algaLinks.linkToRestauranteFormaPagamentoAssociacao(restauranteId, "associar"));
		
		formasPagamentoModel.getContent().forEach(formaPagamentoModel -> {
			formaPagamentoModel.add(algaLinks.linkToRestauranteFormaPagamentoDessasociacao(restauranteId, formaPagamentoModel.getId(), "desassociar"));
		});
		
		return formasPagamentoModel;
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{formaPagamentoId}")
	public ResponseEntity<Void> adicionarFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestauranteService.adicionarFormaPagamento(restauranteId, formaPagamentoId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@DeleteMapping("/{formaPagamentoId}")
	public ResponseEntity<Void> removerFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestauranteService.removerFormaPagamento(restauranteId, formaPagamentoId);
		return ResponseEntity.noContent().build();
	}
	
	
}
