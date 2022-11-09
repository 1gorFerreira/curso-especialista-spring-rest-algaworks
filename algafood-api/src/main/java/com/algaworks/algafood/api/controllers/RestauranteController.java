package com.algaworks.algafood.api.controllers;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repositories.RestauranteRepository;
import com.algaworks.algafood.domain.services.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;

	@GetMapping
	public List<RestauranteModel> listar() {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		return toCollectionModel(restaurantes);
	}

	@GetMapping("/{restauranteId}")
	public ResponseEntity<RestauranteModel> buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		RestauranteModel restauranteModel = toModel(restaurante);
		
		return ResponseEntity.ok(restauranteModel);
	}

	@PostMapping
	public ResponseEntity<RestauranteModel> adicionar(@Valid @RequestBody Restaurante restaurante) {
		try {
			restaurante = cadastroRestauranteService.salvar(restaurante);
			RestauranteModel restauranteDto = toModel(restaurante);
			
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(restaurante.getId()).toUri();
			
			return ResponseEntity.created(uri).body(restauranteDto);
			
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<RestauranteModel> atualizar(@PathVariable Long restauranteId, @Valid @RequestBody Restaurante restaurante) {
		Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);

//		BeanUtils.copyProperties(restaurante, restauranteAtual, 
//			      "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
		restauranteAtual.setNome(restaurante.getNome());
		restauranteAtual.setTaxaFrete(restaurante.getTaxaFrete());
		restauranteAtual.setCozinha(restaurante.getCozinha());

		try {
			restauranteAtual = cadastroRestauranteService.salvar(restauranteAtual);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
		return ResponseEntity.ok(toModel(restauranteAtual));
	}
	
	private RestauranteModel toModel(Restaurante restaurante) {
		CozinhaModel cozinhaModel = new CozinhaModel();
		cozinhaModel.setId(restaurante.getCozinha().getId());
		cozinhaModel.setNome(restaurante.getCozinha().getNome());
		
		RestauranteModel restauranteModel = new RestauranteModel();
		restauranteModel.setId(restaurante.getId());
		restauranteModel.setNome(restaurante.getNome());
		restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());
		restauranteModel.setCozinha(cozinhaModel);
		return restauranteModel;
	}
	
	private List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes){
		return restaurantes.stream().map(restaurante -> toModel(restaurante)).toList();
	}
}
