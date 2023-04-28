package com.algaworks.algafood.api.v1.openapi.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.controllers.EstatisticasController.EstatisticasModel;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.model.filter.VendaDiariaFilter;

@SecurityRequirement(name = "security_auth")
public interface EstatisticasControllerOpenApi {

	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);

	ResponseEntity<byte[]> consultarVendasDiariasPdf(VendaDiariaFilter filtro, String timeOffset);

	EstatisticasModel estatisticas();
}
