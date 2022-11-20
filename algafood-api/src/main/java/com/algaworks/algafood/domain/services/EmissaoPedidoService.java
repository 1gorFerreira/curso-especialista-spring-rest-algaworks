package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repositories.PedidoRepository;

@Service
public class EmissaoPedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@Transactional(readOnly = true)
	public List<PedidoModel> buscarTodos(){
		List<Pedido> pedidos = pedidoRepository.findAll();
		return pedidoModelAssembler.toCollectionModel(pedidos);
	}
	
	@Transactional(readOnly = true)
	public PedidoModel buscar(Long pedidoId) {
		Pedido pedido = buscarOuFalhar(pedidoId);
		return pedidoModelAssembler.toModel(pedido);
	}
	
	
	public Pedido buscarOuFalhar(Long pedidoId) {
		return pedidoRepository.findById(pedidoId)
				.orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}
}
