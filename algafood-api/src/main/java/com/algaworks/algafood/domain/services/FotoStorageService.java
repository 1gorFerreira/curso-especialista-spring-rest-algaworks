package com.algaworks.algafood.domain.services;

import java.io.InputStream;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface FotoStorageService {
	
	void armazenar(NovaFoto novaFoto);
	
	@Builder
	@Getter
	@Setter
	class NovaFoto {
		
		private String nomeArquivo;
		private InputStream inputStream;
		
	}
	
}
