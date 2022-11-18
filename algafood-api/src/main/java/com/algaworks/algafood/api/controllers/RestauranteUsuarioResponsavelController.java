package com.algaworks.algafood.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.domain.services.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {
	
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@GetMapping
	public ResponseEntity<List<UsuarioModel>> listarResponsaveis(@PathVariable Long restauranteId){
		List<UsuarioModel> responsaveis = cadastroRestauranteService.listarResponsaveis(restauranteId);
		return ResponseEntity.ok(responsaveis);
	}
	
	@PutMapping("/{responsavelId}")
	public ResponseEntity<Void> associarResponsavel(@PathVariable Long restauranteId, @PathVariable Long responsavelId){
		cadastroRestauranteService.associarResponsavel(restauranteId, responsavelId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{responsavelId}")
	public ResponseEntity<Void> desassociarResponsavel(@PathVariable Long restauranteId, @PathVariable Long responsavelId){
		cadastroRestauranteService.desassociarResponsavel(restauranteId, responsavelId);
		return ResponseEntity.noContent().build();
	}
}
