package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controllers.CidadeController;
import com.algaworks.algafood.api.controllers.FormaPagamentoController;
import com.algaworks.algafood.api.controllers.PedidoController;
import com.algaworks.algafood.api.controllers.RestauranteController;
import com.algaworks.algafood.api.controllers.RestauranteProdutoController;
import com.algaworks.algafood.api.controllers.UsuarioController;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel>{

	@Autowired
	private ModelMapper modelMapper;
	
	public PedidoModelAssembler() {
		super(PedidoController.class, PedidoModel.class);
	}
	
	public PedidoModel toModel(Pedido pedido) {
		PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
		modelMapper.map(pedido, pedidoModel);
		
		pedidoModel.add(WebMvcLinkBuilder.linkTo(PedidoController.class).withRel("pedidos"));
		
		pedidoModel.getRestaurante().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RestauranteController.class)
				.buscar(pedido.getRestaurante().getId())).withSelfRel());
		
		pedidoModel.getCliente().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
				.buscar(pedido.getCliente().getId())).withSelfRel());
		
		pedidoModel.getFormaPagamento().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormaPagamentoController.class)
				.buscar(pedido.getFormaPagamento().getId(), null)).withSelfRel());
		
		pedidoModel.getEnderecoEntrega().getCidade().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
				.buscar(pedido.getEnderecoEntrega().getCidade().getId())).withSelfRel());
		
		pedidoModel.getItens().forEach(item -> {
	           	item.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RestauranteProdutoController.class)
	                   .buscar(pedidoModel.getRestaurante().getId(), item.getProdutoId()))
	                   .withRel("produto"));
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
