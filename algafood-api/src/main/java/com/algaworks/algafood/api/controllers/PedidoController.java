package com.algaworks.algafood.api.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.domain.services.EmissaoPedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

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
	
	@GetMapping
	public ResponseEntity<List<PedidoResumoModel>> buscarTodos(){
		List<PedidoResumoModel> pedidos = emissaoPedidoService.buscarTodos();
		return ResponseEntity.ok(pedidos);
	}
	
	@GetMapping("/{codigoPedido}")
	public ResponseEntity<PedidoModel> buscar(@PathVariable String codigoPedido){
		PedidoModel pedido = emissaoPedidoService.buscar(codigoPedido);
		return ResponseEntity.ok(pedido);
	}
	
	@PostMapping
	public ResponseEntity<PedidoModel> emitir(@Valid @RequestBody PedidoInput pedidoInput){
		PedidoModel pedido = emissaoPedidoService.emitir(pedidoInput);
	        
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(pedido.getCodigo()).toUri();
		
		return ResponseEntity.created(uri).body(pedido);
	}
}
