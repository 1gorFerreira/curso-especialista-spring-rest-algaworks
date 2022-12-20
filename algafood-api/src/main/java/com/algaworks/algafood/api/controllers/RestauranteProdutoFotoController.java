package com.algaworks.algafood.api.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.services.CatalogoFotoProdutoService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {
	
	@Autowired
	private CatalogoFotoProdutoService catalogoFotoProdutoService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FotoProdutoModel> buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId){
		FotoProdutoModel foto = catalogoFotoProdutoService.buscar(restauranteId, produtoId);
		return ResponseEntity.ok(foto);
	}
	
	@GetMapping // (produces = { MediaType.IMAGE_JPEG_VALUE , MediaType.IMAGE_PNG_VALUE }) -> Implementado em verificarCompatibilidadeMediaType;
	public ResponseEntity<InputStreamResource> servirFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException{
		try {
			InputStreamResource foto = catalogoFotoProdutoService.servirFoto(restauranteId, produtoId, acceptHeader);
			
			MediaType mediaTypeFoto = catalogoFotoProdutoService.getMediaTypeFoto(restauranteId, produtoId);
			
			return ResponseEntity.ok().contentType(mediaTypeFoto).body(foto);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FotoProdutoModel> atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, 
			@Valid FotoProdutoInput fotoProdutoInput) throws IOException {
		
		FotoProdutoModel fotoSalva = catalogoFotoProdutoService.salvar(restauranteId, produtoId, fotoProdutoInput, 
				fotoProdutoInput.getArquivo().getInputStream());
		
		return ResponseEntity.ok(fotoSalva);
	}

}
