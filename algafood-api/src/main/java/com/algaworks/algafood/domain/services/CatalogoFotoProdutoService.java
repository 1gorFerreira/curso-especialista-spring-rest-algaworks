package com.algaworks.algafood.domain.services;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repositories.ProdutoRepository;
import com.algaworks.algafood.domain.services.FotoStorageService.FotoRecuperada;
import com.algaworks.algafood.domain.services.FotoStorageService.NovaFoto;

@Service
public class CatalogoFotoProdutoService {
	
	@Autowired
	private FotoProdutoModelAssembler fotoProdutoModelAssembler;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	
	@Transactional(readOnly = true)
	public FotoProdutoModel buscar(Long restauranteId, Long produtoId) {
		FotoProduto fotoProduto = buscarOuFalhar(restauranteId, produtoId);
		return fotoProdutoModelAssembler.toModel(fotoProduto);
	}
	
	@Transactional
	public FotoRecuperada servirFoto(Long restauranteId, Long produtoId, String acceptHeader) throws HttpMediaTypeNotAcceptableException {
		FotoProduto fotoProduto = buscarOuFalhar(restauranteId, produtoId);
		
		//Verificando se o conteúdo informado no header é compatível com a foto armazenada;
		
		MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
		List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);
		
		verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);
		
		//
		
		FotoRecuperada fotoRecuperada = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());
		
		return fotoRecuperada;
	}
	
	
	@Transactional
	public FotoProdutoModel salvar(Long restauranteId, Long produtoId, FotoProdutoInput fotoProdutoInput, InputStream dadosArquivo) {
		Produto produto = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
		
		FotoProduto foto = new FotoProduto();
		foto.setProduto(produto);
		foto.setDescricao(fotoProdutoInput.getDescricao());
		foto.setContentType(fotoProdutoInput.getArquivo().getContentType());
		foto.setTamanho(fotoProdutoInput.getArquivo().getSize());
		foto.setNomeArquivo(fotoProdutoInput.getArquivo().getOriginalFilename());
		
		Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);
		String nomeArquivoExistente = null;
		
		if(fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			produtoRepository.delete(fotoExistente.get());
		}
		
		String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
		
		foto.setNomeArquivo(nomeNovoArquivo);
		foto = produtoRepository.save(foto);
		produtoRepository.flush(); // Descarregar tudo que estiver na fila do EntityManager;
		
		NovaFoto novaFoto = NovaFoto.builder()
				.nomeArquivo(foto.getNomeArquivo())
				.contentType(foto.getContentType())
				.inputStream(dadosArquivo)
				.build();
		
		fotoStorageService.substituir(nomeArquivoExistente, novaFoto);
		
		return fotoProdutoModelAssembler.toModel(foto);
	}
	
	@Transactional
	public void deletar(Long restauranteId, Long produtoId) {
		FotoProduto fotoProduto = buscarOuFalhar(restauranteId, produtoId);
		
		produtoRepository.delete(fotoProduto);
		produtoRepository.flush();
		
		fotoStorageService.remover(fotoProduto.getNomeArquivo());
	}
	
	
	@Transactional(readOnly = true)
	public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return produtoRepository.findFotoById(restauranteId, produtoId)
				.orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
	}
	
	public MediaType getMediaTypeFoto(Long restauranteId, Long produtoId) {
		FotoProduto fotoProduto = buscarOuFalhar(restauranteId, produtoId);
		
		return MediaType.parseMediaType(fotoProduto.getContentType());
	}
	
	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
		boolean compativel = mediaTypesAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		
		if(!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
		}
	}
	
}
