package com.algaworks.algafood.api.v1.controllers;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.api.v1.openapi.controller.PedidoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.filter.PedidoFilter;
import com.algaworks.algafood.domain.services.EmissaoPedidoService;

@RestController
@RequestMapping(path = "/v1/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

	@Autowired
	private EmissaoPedidoService emissaoPedidoService;
	
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
//		List<PedidoResumoModel> pedidosModel = emissaoPedidoService.buscarTodos();
//		
//		MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);
//		
//		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//		filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
//		
//		if(StringUtils.isNotBlank(campos)) {
//			filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
//		}
//				
//		pedidosWrapper.setFilters(filterProvider);
//		
//		return pedidosWrapper;
//	}
	
	@CheckSecurity.Pedidos.PodePesquisar
	@Override
	@GetMapping
	public ResponseEntity<PagedModel<PedidoResumoModel>> pesquisar(PedidoFilter filtro, Pageable pageable){
		PagedModel<PedidoResumoModel> pedidos = emissaoPedidoService.buscarTodos(filtro, pageable);
		return ResponseEntity.ok(pedidos);
	}
	
	@CheckSecurity.Pedidos.PodeBuscar
	@Override
	@GetMapping("/{codigoPedido}")
	public ResponseEntity<PedidoModel> buscar(@PathVariable String codigoPedido){
		PedidoModel pedido = emissaoPedidoService.buscar(codigoPedido);
		return ResponseEntity.ok(pedido);
	}
	
	@CheckSecurity.Pedidos.PodeCriar
	@Override
	@PostMapping
	public ResponseEntity<PedidoModel> emitir(@Valid @RequestBody PedidoInput pedidoInput){
		PedidoModel pedido = emissaoPedidoService.emitir(pedidoInput);
	        
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(pedido.getCodigo()).toUri();
		
		return ResponseEntity.created(uri).body(pedido);
	}
}
