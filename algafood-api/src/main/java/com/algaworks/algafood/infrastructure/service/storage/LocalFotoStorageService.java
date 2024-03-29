package com.algaworks.algafood.infrastructure.service.storage;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.services.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFotoStorageService implements FotoStorageService {

	// Estamos usando o NovaFoto e não o MultipartFile para não ficar dependente de um pacote externo;
	// Perceba que o método MultipartFile.transferTo faz exatamente o que estamos fazendo com o FileCopyUtils.copy();

	@Autowired
	private StorageProperties storageProperties;
	
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
	
	@Override
	public void remover(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			
			Files.deleteIfExists(arquivoPath);
		} catch (IOException e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
	}
	
	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			
			FotoRecuperada fotoRecuperada = FotoRecuperada.builder()
					.inputStream(Files.newInputStream(arquivoPath))
					.build();
			
			return fotoRecuperada;
		} catch (IOException e) {
			throw new StorageException("Não foi possível recuperar arquivo.", e);
		}
	}
	
	
	private Path getArquivoPath(String nomeArquivo) {
		// diretorio da pasta + nome do arquivo = Path, resolve faz essa união;
		return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
	}

}
