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
	public void confirmar(String codigoPedido) {
		Pedido pedido = cadastroPedidoService.buscarOuFalhar(codigoPedido);
		pedido.confirmar();
	}
	
	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = cadastroPedidoService.buscarOuFalhar(codigoPedido);
		pedido.cancelar();
	}
	
	@Transactional
	public void entregar(String codigoPedido) {
		Pedido pedido = cadastroPedidoService.buscarOuFalhar(codigoPedido);
		pedido.entregar();
	}
	
	
}
