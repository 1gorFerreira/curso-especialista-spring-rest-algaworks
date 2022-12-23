package com.algaworks.algafood.domain.services;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

public interface EnvioEmailService {

	void enviar(Mensagem mensagem);
	
	@Getter
	@Builder
	class Mensagem {
		
		@Singular // cria o String destinatario;
		private Set<String> destinatarios;
		
		@NonNull
		private String assunto;
		
		@NonNull
		private String corpo;
	}
}
