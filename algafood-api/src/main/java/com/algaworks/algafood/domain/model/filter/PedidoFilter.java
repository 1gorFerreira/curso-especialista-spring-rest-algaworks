package com.algaworks.algafood.domain.model.filter;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//Classe DTO para representar os filtros que queremos pesquisar;
public class PedidoFilter {

	private Long clienteId;

	private Long restauranteId;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataCriacaoInicio;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataCriacaoFim;
	
}
