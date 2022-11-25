package com.algaworks.algafood.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.services.FluxoPedidoService;

@RestController
@RequestMapping("/pedidos/{pedidoId}")
public class FluxoPedidoController {

	@Autowired
	private FluxoPedidoService fluxoPedidoService;
	
	@PutMapping("/confirmacao")
	public ResponseEntity<Void> confirmar(@PathVariable Long pedidoId){
		fluxoPedidoService.confirmar(pedidoId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/cancelamento")
	public ResponseEntity<Void> cancelar(@PathVariable Long pedidoId){
		fluxoPedidoService.cancelar(pedidoId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/entrega")
	public ResponseEntity<Void> entregar(@PathVariable Long pedidoId){
		fluxoPedidoService.entregar(pedidoId);
		return ResponseEntity.noContent().build();
	}
}
