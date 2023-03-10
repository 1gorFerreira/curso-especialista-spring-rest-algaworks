package com.algaworks.algafood.api.v1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.openapi.controller.EstatisticasControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.model.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.services.VendaQueryService;
import com.algaworks.algafood.domain.services.VendaReportService;

@RestController
@RequestMapping(path = "/v1/estatisticas")
public class EstatisticasController implements EstatisticasControllerOpenApi{

	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Autowired
	private VendaReportService vendaReportService;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	// Se o usuário da API não especificar nenhum Offset, retornaremos em UTC (+00:00):
	@CheckSecurity.Estatisticas.PodeConsultar
	@GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VendaDiaria> consultarVendasDiarias(
			VendaDiariaFilter filtro, 
			@RequestParam(required = false, defaultValue = "+00:00") String timeOffset){
		List<VendaDiaria> vendaDiaria = vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
		return vendaDiaria;
	}
	
	@CheckSecurity.Estatisticas.PodeConsultar
	@GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> consultarVendasDiariasPdf(
			VendaDiariaFilter filtro, 
			@RequestParam(required = false, defaultValue = "+00:00") String timeOffset){
		
		byte[] bytesPdf = vendaReportService.emitirVendasDiarias(filtro, timeOffset);
	
		var headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf"); // attachment diz que é para fazer download;
		
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.headers(headers)
				.body(bytesPdf);
	}
	
	@CheckSecurity.Estatisticas.PodeConsultar
	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public EstatisticasModel estatisticas() {
	    var estatisticasModel = new EstatisticasModel();
	    
	    estatisticasModel.add(algaLinks.linkToEstatisticasVendasDiarias("vendas-diarias"));
	    
	    return estatisticasModel;
	}       
	
	public static class EstatisticasModel extends RepresentationModel<EstatisticasModel> {
	}
	
}
