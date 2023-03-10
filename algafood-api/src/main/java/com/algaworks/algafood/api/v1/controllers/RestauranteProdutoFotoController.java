package com.algaworks.algafood.api.v1.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.model.FotoProdutoModel;
import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.services.CatalogoFotoProdutoService;
import com.algaworks.algafood.domain.services.FotoStorageService.FotoRecuperada;

@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/produtos/{produtoId}/foto", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi{
	
	@Autowired
	private CatalogoFotoProdutoService catalogoFotoProdutoService;
	
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public ResponseEntity<FotoProdutoModel> buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId){
		FotoProdutoModel foto = catalogoFotoProdutoService.buscar(restauranteId, produtoId);
		return ResponseEntity.ok(foto);
	}
	
	// As fotos dos produtos ficarão públicas (não precisa de autorização para acessá-las)
	@GetMapping(produces = MediaType.ALL_VALUE) // (produces = { MediaType.IMAGE_JPEG_VALUE , MediaType.IMAGE_PNG_VALUE }) -> Implementado em verificarCompatibilidadeMediaType;
	public ResponseEntity<?> servirFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException{
		try {
			FotoRecuperada fotoRecuperada = catalogoFotoProdutoService.servirFoto(restauranteId, produtoId, acceptHeader);
			
			MediaType mediaTypeFoto = catalogoFotoProdutoService.getMediaTypeFoto(restauranteId, produtoId);
			
			if (fotoRecuperada.temUrl()) {
				System.out.println(fotoRecuperada.getUrl());
				return ResponseEntity
						.status(HttpStatus.FOUND)
						.header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
						.build();
			} else {
				return ResponseEntity.ok()
						.contentType(mediaTypeFoto)
						.body(new InputStreamResource(fotoRecuperada.getInputStream()));
			}
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FotoProdutoModel> atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, 
			@Valid FotoProdutoInput fotoProdutoInput) throws IOException {
		
		FotoProdutoModel fotoSalva = catalogoFotoProdutoService.salvar(restauranteId, produtoId, fotoProdutoInput, 
				fotoProdutoInput.getArquivo().getInputStream());
		
		return ResponseEntity.ok(fotoSalva);
	}
	
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@DeleteMapping
	public ResponseEntity<Void> excluir(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		catalogoFotoProdutoService.deletar(restauranteId, produtoId);
		return ResponseEntity.noContent().build();
	}

}
