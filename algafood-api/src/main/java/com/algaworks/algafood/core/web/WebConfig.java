package com.algaworks.algafood.core.web;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	//Filtro que faz o trabalho de ao receber uma requisicao, na hora de dar a resposta ele gera um hash
	//da resposta e poe o cabecalho Etag, e tambem verifica se o hash dessa resposta coincide com o If-None-Match
	@Bean
	public Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
	
}
