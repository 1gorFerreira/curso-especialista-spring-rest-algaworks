package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controllers.PedidoController;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	public PedidoModelAssembler() {
		super(PedidoController.class, PedidoModel.class);
	}
	
	
	public PedidoModel toModel(Pedido pedido) {
		PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
		modelMapper.map(pedido, pedidoModel);
		
		pedidoModel.add(algaLinks.linkToPedidos());
		
//		pedidoModel.add(WebMvcLinkBuilder.linkTo(PedidoController.class).withRel("pedidos"));
		
		if(pedido.podeSerConfirmado()) {
			pedidoModel.add(algaLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
		}
		
		if(pedido.podeSerEntregue()) {
			pedidoModel.add(algaLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
		}
		
		if(pedido.podeSerCancelado()) {
			pedidoModel.add(algaLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
		}
		
		pedidoModel.getRestaurante().add(algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		
		pedidoModel.getCliente().add(algaLinks.linkToUsuario(pedido.getCliente().getId()));
		
		pedidoModel.getFormaPagamento().add(algaLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));
		
		pedidoModel.getEnderecoEntrega().getCidade().add(algaLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
		
		pedidoModel.getItens().forEach(item -> {
	           	item.add(algaLinks.linkToProduto(pedido.getRestaurante().getId(), item.getProdutoId(), "produto"));
	    });
		
//		for(ItemPedidoModel itemPedido : pedidoModel.getItens()) {
//			itemPedido.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RestauranteProdutoController.class)
//					.buscar(pedido.getRestaurante().getId(), itemPedido.getProdutoId())).withRel("produto"));
//		}
		
		return pedidoModel;
	}
	
	public List<PedidoModel> toCollectionModel(Collection<Pedido> pedidos){
		return pedidos.stream().map(pedido -> toModel(pedido)).toList();
	}
}
