package com.algaworks.algafood.domain.v1.services;

import com.algaworks.algafood.domain.v1.model.filter.VendaDiariaFilter;

public interface VendaReportService {
	
	byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
	
}
