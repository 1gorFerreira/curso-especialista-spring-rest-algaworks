package com.algaworks.algafood.core.squiggly;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;

@Configuration
public class SquigglyConfig {

	//Configurando para que sempre que uma requisição HTTP for chamada, vai passar por esse filtro;
	@Bean
	public FilterRegistrationBean<SquigglyRequestFilter> squigglyRequestFilter(ObjectMapper objectMapper){
		Squiggly.init(objectMapper, new RequestSquigglyContextProvider("campos", null)); //Padrão = Filter
		
		//var urlPatterns = Arrays.asList("/pedidos/*", "/restaurantes/*");
		
		var filterRegistration = new FilterRegistrationBean<SquigglyRequestFilter>();
		filterRegistration.setFilter(new SquigglyRequestFilter());
		filterRegistration.setOrder(1);
		//filterRegistration.setUrlPatterns(urlPatterns); Dizendo os caminhos que quer que o Squiggly funcione;
		
		return filterRegistration;
	}
	
}
