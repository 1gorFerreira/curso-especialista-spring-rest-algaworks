package com.algaworks.algafood.api.v1.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.algaworks.algafood.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.v1.model.RestauranteApenasNomeModel;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repositories.RestauranteRepository;
import com.algaworks.algafood.domain.services.CadastroRestauranteService;

@RestController
@RequestMapping(path = "/v1/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenApi{

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;

	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	@Autowired
	private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;
	
	@Autowired
	private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler;
	
//	@JsonView(RestauranteView.Resumo.class)
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public CollectionModel<RestauranteBasicoModel> listar() {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		return restauranteBasicoModelAssembler.toCollectionModel(restaurantes);
	}
	
//	@JsonView(RestauranteView.ApenasNome.class)
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping(params = "projecao=apenas-nome") // ?projecao=resumo
	public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
//		return listar();
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		return restauranteApenasNomeModelAssembler.toCollectionModel(restaurantes);
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

	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping("/{restauranteId}")
	public ResponseEntity<RestauranteModel> buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		RestauranteModel restauranteModel = restauranteModelAssembler.toModel(restaurante);
		
		return ResponseEntity.ok(restauranteModel);
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
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

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
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
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> ativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.ativar(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.inativar(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		cadastroRestauranteService.ativar(restauranteIds);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		cadastroRestauranteService.inativar(restauranteIds);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> abrir(@PathVariable Long restauranteId) {
		cadastroRestauranteService.abrir(restauranteId);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> fechar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.fechar(restauranteId);
		return ResponseEntity.noContent().build();
	}

}
