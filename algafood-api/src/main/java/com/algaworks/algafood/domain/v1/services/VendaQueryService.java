package com.algaworks.algafood.domain.v1.services;

import java.util.List;

import com.algaworks.algafood.domain.v1.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.v1.model.filter.VendaDiariaFilter;

public interface VendaQueryService {

	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
	
}
