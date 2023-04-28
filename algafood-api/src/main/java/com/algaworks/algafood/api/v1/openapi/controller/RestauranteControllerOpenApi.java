package com.algaworks.algafood.api.v1.openapi.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.RestauranteApenasNomeModel;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;

@SecurityRequirement(name = "security_auth")
public interface RestauranteControllerOpenApi {

//  @JsonView(RestauranteView.Resumo.class)
	CollectionModel<RestauranteBasicoModel> listar();

	CollectionModel<RestauranteApenasNomeModel> listarApenasNomes();

	ResponseEntity<RestauranteModel> buscar(Long restauranteId);

	ResponseEntity<RestauranteModel> adicionar(RestauranteInput restauranteInput);

	ResponseEntity<RestauranteModel> atualizar(Long restauranteId, RestauranteInput restauranteInput);

	ResponseEntity<Void> ativar(Long restauranteId);

	ResponseEntity<Void> inativar(Long restauranteId);

	ResponseEntity<Void> ativarMultiplos(List<Long> restauranteIds);

	ResponseEntity<Void> inativarMultiplos(List<Long> restauranteIds);

	ResponseEntity<Void> abrir(Long restauranteId);

	ResponseEntity<Void> fechar(Long restauranteId);

}
