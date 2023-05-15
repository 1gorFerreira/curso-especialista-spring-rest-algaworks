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
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteUsuarioResponsavelControllerOpenApi;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.services.CadastroRestauranteService;

@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/responsaveis", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteUsuarioResponsavelController implements RestauranteUsuarioResponsavelControllerOpenApi{
	
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity;
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@Override
	@GetMapping
	public ResponseEntity<CollectionModel<UsuarioModel>> listarResponsaveis(@PathVariable Long restauranteId){
		CollectionModel<UsuarioModel> responsaveis = cadastroRestauranteService.listarResponsaveis(restauranteId);
		responsaveis.removeLinks()
				.add(algaLinks.linkToResponsaveisRestaurante(restauranteId));
		
		if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
			responsaveis.add(algaLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));
			
			responsaveis.getContent().forEach(responsavelModel -> {
				responsavelModel.add(algaLinks.linkToRestauranteResponsavelDesassociacao(restauranteId, responsavelModel.getId(), "desassociar"));
			});
		}
		
		return ResponseEntity.ok(responsaveis);
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Override
	@PutMapping("/{responsavelId}")
	public ResponseEntity<Void> associarResponsavel(@PathVariable Long restauranteId, @PathVariable Long responsavelId){
		cadastroRestauranteService.associarResponsavel(restauranteId, responsavelId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@Override
	@DeleteMapping("/{responsavelId}")
	public ResponseEntity<Void> desassociarResponsavel(@PathVariable Long restauranteId, @PathVariable Long responsavelId){
		cadastroRestauranteService.desassociarResponsavel(restauranteId, responsavelId);
		return ResponseEntity.noContent().build();
	}
}
