package com.algaworks.algafood.domain.v1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.v1.assembler.ProdutoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.api.v1.model.input.ProdutoInput;
import com.algaworks.algafood.domain.v1.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.v1.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.v1.model.Produto;
import com.algaworks.algafood.domain.v1.model.Restaurante;
import com.algaworks.algafood.domain.v1.repositories.ProdutoRepository;
import com.algaworks.algafood.domain.v1.repositories.RestauranteRepository;

@Service
public class CadastroProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private ProdutoModelAssembler produtoModelAssembler;
	
	@Autowired
	private ProdutoInputDisassembler produtoInputDisassembler;
	
	@Transactional(readOnly = true)
	public CollectionModel<ProdutoModel> listarProdutos(Long restauranteId, Boolean incluirInativos){
		Restaurante restaurante = restauranteRepository.findById(restauranteId)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
		
		List<Produto> produtos = null;
		
		if(incluirInativos) {
			produtos = produtoRepository.findTodosByRestaurante(restaurante);
		} else {			
			produtos = produtoRepository.findAtivosByRestaurante(restaurante);
		}
		
		return produtoModelAssembler.toCollectionModel(produtos);
	}
	
	@Transactional(readOnly = true)
	public ProdutoModel buscar(Long restauranteId, Long produtoId) {
		Produto produto = buscarOuFalhar(restauranteId, produtoId);
		return produtoModelAssembler.toModel(produto);
	}
	
	@Transactional
	public ProdutoModel adicionar(Long restauranteId, ProdutoInput produtoInput) {
		Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
		
		Restaurante restaurante = restauranteRepository.findById(restauranteId)
				.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
		
		produto.setRestaurante(restaurante);
		
		produto = produtoRepository.save(produto);
		return produtoModelAssembler.toModel(produto);
	}
	
	@Transactional
	public ProdutoModel atualizar(Long restauranteId, Long produtoId, ProdutoInput produtoInput) {
		Produto produtoAtual = buscarOuFalhar(restauranteId, produtoId);
		
		produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);
		
		produtoAtual = produtoRepository.save(produtoAtual);
		return produtoModelAssembler.toModel(produtoAtual);
	}
	
	@Transactional(readOnly = true)
	public Produto buscarOuFalhar(Long restauranteId, Long produtoId) {
		Produto produto = produtoRepository.findById(restauranteId, produtoId)
				.orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId, restauranteId));
		return produto;
	}
	
}
