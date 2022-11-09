package com.algaworks.algafood.api.model.mixin;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public abstract class RestauranteMixin {
	
//	@JsonIgnoreProperties("hibernateLazyInitializer") //Com essa anotação podemos ignorar alguma própriedade dentro de cozinha ao inves do objeto cozinha;
	@JsonIgnoreProperties(value = "nome", allowGetters = true) //AllowGetters não irá ignorar no getNome, apenas no setNome;
	private Cozinha cozinha;

	@JsonIgnore
	private Endereco endereco;
	
//	@JsonIgnore
	private OffsetDateTime dataCadastro;
	
//	@JsonIgnore
	private OffsetDateTime dataAtualizacao;
	
	@JsonIgnore
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
	
	@JsonIgnore
	private List<Produto> produtos = new ArrayList<>(); 
	
}