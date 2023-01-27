package com.algaworks.algafood.api.controllers;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.api.openapi.controller.FormaPagamentoControllerOpenApi;
import com.algaworks.algafood.domain.services.CadastroFormaPagamentoService;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi{

	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FormaPagamentoModel>> listar(ServletWebRequest request){
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = cadastroFormaPagamentoService.gerandoETag();
		
		if (request.checkNotModified(eTag)) {
			return null;
		}
		
		List<FormaPagamentoModel> formasPagamento = cadastroFormaPagamentoService.buscarTodos();
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				.eTag(eTag)
				.body(formasPagamento);
	}
	
	@GetMapping(path = "/{formaPagamentoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long formaPagamentoId, ServletWebRequest request){
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = cadastroFormaPagamentoService.gerandoETagParaRecursoUnico(formaPagamentoId);
		
		if (request.checkNotModified(eTag)) {
			return null;
		}
		
		FormaPagamentoModel formaPagamento = cadastroFormaPagamentoService.buscar(formaPagamentoId);
		return ResponseEntity.ok()
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
//				.cacheControl(CacheControl.noCache()) // Diz que sempre havera uma validacao no servidor (Sempre em stale);
//				.cacheControl(CacheControl.noStore()) // Diz que nenhum cache pode armazenar a resposta;
				.eTag(eTag)
				.body(formaPagamento);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FormaPagamentoModel> adicionar(@Valid @RequestBody FormaPagamentoInput formaPagamentoInput){
		FormaPagamentoModel formaPagamento = cadastroFormaPagamentoService.adicionar(formaPagamentoInput);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(formaPagamento.getId()).toUri();
		return ResponseEntity.created(uri).body(formaPagamento);
	}
	
	@PutMapping(path = "/{formaPagamentoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FormaPagamentoModel> atualizar(@Valid @PathVariable Long formaPagamentoId, @RequestBody FormaPagamentoInput formaPagamentoInput){
		FormaPagamentoModel formaPagamentoModel = cadastroFormaPagamentoService.atualizar(formaPagamentoId, formaPagamentoInput);
		return ResponseEntity.ok(formaPagamentoModel);
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	public ResponseEntity<Void> deletar(@PathVariable Long formaPagamentoId){
		cadastroFormaPagamentoService.deletar(formaPagamentoId);
		return ResponseEntity.noContent().build();
	}
}
