package com.algaworks.algafood.api.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.api.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repositories.RestauranteRepository;
import com.algaworks.algafood.domain.services.CadastroRestauranteService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController implements RestauranteControllerOpenApi{

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;

	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	@JsonView(RestauranteView.Resumo.class)
	@GetMapping
	public List<RestauranteModel> listar() {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		return restauranteModelAssembler.toCollectionModel(restaurantes);
	}
	
	@JsonView(RestauranteView.ApenasNome.class)
	@GetMapping(params = "projecao=apenas-nome") // ?projecao=resumo
	public List<RestauranteModel> listarApenasNomes() {
		return listar();
	}
	
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//		List<RestauranteModel> restaurantesModel = restauranteModelAssembler.toCollectionModel(restaurantes);
//		
//		//Empacotando:
//		MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restaurantesModel);
//		
//		restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);
//		
//		if("apenas-nome".equals(projecao)) {
//			//Indicando que desejo usar um SerializationView:
//			restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
//		} else if ("completo".equals(projecao)) {
//			restaurantesWrapper.setSerializationView(null);
//		}
//		
//		return restaurantesWrapper;
//	}
	
//	@GetMapping
//	public List<RestauranteModel> listar() {
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//		return restauranteModelAssembler.toCollectionModel(restaurantes);
//	}
//	
//	@JsonView(RestauranteView.Resumo.class)
//	@GetMapping(params = "projecao=resumo") // ?projecao=resumo
//	public List<RestauranteModel> listarResumido() {
//		return listar();
//	}
//	
//	@JsonView(RestauranteView.ApenasNome.class)
//	@GetMapping(params = "projecao=apenas-nome") // ?projecao=apenas-nome
//	public List<RestauranteModel> listarApenasNomes() {
//		return listar();
//	}

	@GetMapping("/{restauranteId}")
	public ResponseEntity<RestauranteModel> buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		RestauranteModel restauranteModel = restauranteModelAssembler.toModel(restaurante);
		
		return ResponseEntity.ok(restauranteModel);
	}

	@PostMapping
	public ResponseEntity<RestauranteModel> adicionar(@Valid @RequestBody RestauranteInput restauranteInput) {
		try {
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
			restaurante = cadastroRestauranteService.salvar(restaurante);
			
			RestauranteModel restauranteDto = restauranteModelAssembler.toModel(restaurante);
			
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(restaurante.getId()).toUri();
			
			return ResponseEntity.created(uri).body(restauranteDto);
			
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<RestauranteModel> atualizar(@PathVariable Long restauranteId, @Valid @RequestBody RestauranteInput restauranteInput) {
		
		Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);

		restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);

		try {
			restauranteAtual = cadastroRestauranteService.salvar(restauranteAtual);
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
		return ResponseEntity.ok(restauranteModelAssembler.toModel(restauranteAtual));
	}
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.inativar(restauranteId);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		cadastroRestauranteService.ativar(restauranteIds);
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		cadastroRestauranteService.inativar(restauranteIds);
	}
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
		cadastroRestauranteService.abrir(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.fechar(restauranteId);
	}

}
