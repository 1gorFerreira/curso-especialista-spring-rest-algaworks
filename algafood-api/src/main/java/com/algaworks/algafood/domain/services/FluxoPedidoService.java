package com.algaworks.algafood.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Pedido;

@Service
public class FluxoPedidoService {

	@Autowired
	private EmissaoPedidoService cadastroPedidoService;
	
	@Transactional
	public void confirmar(Long pedidoId) {
		Pedido pedido = cadastroPedidoService.buscarOuFalhar(pedidoId);
		pedido.confirmar();
	}
	
	@Transactional
	public void cancelar(Long pedidoId) {
		Pedido pedido = cadastroPedidoService.buscarOuFalhar(pedidoId);
		pedido.cancelar();
	}
	
	@Transactional
	public void entregar(Long pedidoId) {
		Pedido pedido = cadastroPedidoService.buscarOuFalhar(pedidoId);
		pedido.entregar();
	}
	
	
}
