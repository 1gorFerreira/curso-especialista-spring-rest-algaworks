package com.algaworks.algafood.api.controllers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repositories.RestauranteRepository;
import com.algaworks.algafood.domain.services.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;

	@GetMapping
	public List<Restaurante> listar() {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		
//		System.out.println("O nome da cozinha é:");
//		restaurantes.get(0).getCozinha().getNome(); A cozinha só será procurada na query quando algum método dela for chamado (estando em Lazy loading);
		
		return restaurantes;
	}

	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(
				() -> new EntidadeNaoEncontradaException("Não existe cadastro de restaurante com código: " + restauranteId));
		
		return ResponseEntity.ok(restaurante);
	}

	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = cadastroRestauranteService.salvar(restaurante);
			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
		try {
			Restaurante restauranteAtual = restauranteRepository.findById(restauranteId).orElseThrow(
					() -> new EntidadeNaoEncontradaException("Não existe cadastro de restaurante com código: " + restauranteId));;

//			BeanUtils.copyProperties(restaurante, restauranteAtual, 
//			        "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
			restauranteAtual.setNome(restaurante.getNome());
			restauranteAtual.setTaxaFrete(restaurante.getTaxaFrete());
			restauranteAtual.setCozinha(restaurante.getCozinha());

			restauranteAtual = cadastroRestauranteService.salvar(restauranteAtual);
			return ResponseEntity.ok(restauranteAtual);

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos){
		Restaurante restauranteAtual = restauranteRepository.findById(restauranteId).orElseThrow(
				() -> new EntidadeNaoEncontradaException("Não existe cadastro de restaurante com código: " + restauranteId));
		
		merge(campos, restauranteAtual);
		
		return atualizar(restauranteId, restauranteAtual);
	}

	//Esse método tem como função mesclar os valores que estão dentro dos campos para o restaurante atual;
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			field.setAccessible(true);
			
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			
			System.out.println(nomePropriedade + "=" + valorPropriedade + "=" + novoValor);
			
			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}
}
