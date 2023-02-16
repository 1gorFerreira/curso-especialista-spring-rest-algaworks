package com.algaworks.algafood.domain.v1.event;

import com.algaworks.algafood.domain.v1.model.Pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoCanceladoEvent {

	private Pedido pedido;
	
}
