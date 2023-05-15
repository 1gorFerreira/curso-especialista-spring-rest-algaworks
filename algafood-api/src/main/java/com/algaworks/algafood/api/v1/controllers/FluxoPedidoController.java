package com.algaworks.algafood.api.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.openapi.controller.FluxoPedidoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.services.FluxoPedidoService;

@RestController
@RequestMapping(path = "/v1/pedidos/{codigoPedido}", produces = MediaType.APPLICATION_JSON_VALUE)
public class FluxoPedidoController implements FluxoPedidoControllerOpenApi{

	@Autowired
	private FluxoPedidoService fluxoPedidoService;
	
	@CheckSecurity.Pedidos.PodeGerenciar
	@Override
	@PutMapping("/confirmacao")
	public ResponseEntity<Void> confirmar(@PathVariable String codigoPedido){
		fluxoPedidoService.confirmar(codigoPedido);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Pedidos.PodeGerenciar
	@Override
	@PutMapping("/cancelamento")
	public ResponseEntity<Void> cancelar(@PathVariable String codigoPedido){
		fluxoPedidoService.cancelar(codigoPedido);
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.Pedidos.PodeGerenciar
	@Override
	@PutMapping("/entrega")
	public ResponseEntity<Void> entregar(@PathVariable String codigoPedido){
		fluxoPedidoService.entregar(codigoPedido);
		return ResponseEntity.noContent().build();
	}
}
