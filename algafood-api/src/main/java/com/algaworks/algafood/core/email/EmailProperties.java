package com.algaworks.algafood.core.email;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {

	@NotNull
	private String remetente;
	
	@NotNull
	private Sandbox sandbox = new Sandbox();
	
	// Atribuimos FAKE como padrão
	// Isso evita o problema de enviar e-mails de verdade caso você esqueça
	// de definir a propriedade
	private Implementacao impl = Implementacao.FAKE;
	
	public enum Implementacao {
		
		FAKE, SMTP, SANDBOX
		
	}
	
	@Getter
	@Setter
	public class Sandbox {
	    
	    private String destinatario;
	    
	}        
	
}
