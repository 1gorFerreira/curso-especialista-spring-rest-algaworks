package com.algaworks.algafood.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.services.EmissaoPedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private EmissaoPedidoService emissaoPedidoService;
	
	@GetMapping
	public ResponseEntity<List<PedidoModel>> buscarTodos(){
		List<PedidoModel> pedidos = emissaoPedidoService.buscarTodos();
		return ResponseEntity.ok(pedidos);
	}
	
	@GetMapping("/{pedidoId}")
	public ResponseEntity<PedidoModel> buscar(@PathVariable Long pedidoId){
		PedidoModel pedido = emissaoPedidoService.buscar(pedidoId);
		return ResponseEntity.ok(pedido);
	}
}
