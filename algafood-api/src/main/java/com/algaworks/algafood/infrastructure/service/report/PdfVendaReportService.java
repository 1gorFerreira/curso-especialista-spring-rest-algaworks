package com.algaworks.algafood.infrastructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.model.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.services.VendaQueryService;
import com.algaworks.algafood.domain.services.VendaReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PdfVendaReportService implements VendaReportService {
	
	@Autowired
	private VendaQueryService vendaQueryService;

	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		try {
			// Busca um recurso dentro do projeto;
			var inputStream = this.getClass().getResourceAsStream( 
					"/relatorios/vendas-diarias.jasper"); 
			
			//Configurando o Locale;
			var parametros = new HashMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			
			//Dizendo que o tipo de dado que será utilizado (Nesse caso uma coleção JAVA);
			var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
			var dataSource = new JRBeanCollectionDataSource(vendasDiarias);
			
			// jasperPrint é um objeto que representa um relatório preenchido;
			var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
			
			// Criando o PDF;
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
		}
	}

}
