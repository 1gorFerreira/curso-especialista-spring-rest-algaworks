package com.algaworks.algafood.core.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {
	
	//LocalValidatorFactoryBean é uma classe que faz a integração e configuração do bean validation com o spring;
	//Estamos customizando dizendo que o Validation Message source padrão será o messageSource, assim ele usará o message.properties;
	//Se não especificarmos, ele usará como padrão o arquivo de mensagens validationmessages.properties;
	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}
	
}
