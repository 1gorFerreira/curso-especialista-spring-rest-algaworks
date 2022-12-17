package com.algaworks.algafood.domain.services;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface FotoStorageService {
	
	void armazenar(NovaFoto novaFoto);
	
	void remover(String nomeArquivo);
	
	default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
		this.armazenar(novaFoto);
		
		if(nomeArquivoAntigo != null) {
			this.remover(nomeArquivoAntigo);
		}
	}
	
	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString() + "_" + nomeOriginal;
	}
	
	@Builder
	@Getter
	@Setter
	class NovaFoto {
		
		private String nomeArquivo;
		private InputStream inputStream;
		
	}
	
}
