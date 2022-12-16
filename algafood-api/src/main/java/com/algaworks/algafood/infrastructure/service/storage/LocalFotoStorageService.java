package com.algaworks.algafood.infrastructure.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.flywaydb.core.internal.util.FileCopyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.services.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService {

	// Estamos usando o NovaFoto e não o MultipartFile para não ficar dependente de um pacote externo;
	// Perceba que o método MultipartFile.transferTo faz exatamente o que estamos fazendo com o FileCopyUtils.copy();
	
	@Value("${algafood.storage.local.diretorio-fotos}")
	private Path diretorioFotos;
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
		//					   Path.of("/Users/igor/Desktop/catalogo", novaFoto.getNomeArquivo());
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
			
			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
			
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
	}
	
	private Path getArquivoPath(String nomeArquivo) {
		// diretorio da pasta + nome do arquivo = Path, resolve faz essa união;
		return diretorioFotos.resolve(Path.of(nomeArquivo));
	}

	
	
}
