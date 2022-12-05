package com.algaworks.algafood.domain.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.ItemPedido;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repositories.PedidoRepository;
import com.algaworks.algafood.domain.repositories.filter.PedidoFilter;
import com.algaworks.algafood.infrastructure.repositories.specs.PedidoSpecs;

@Service
public class EmissaoPedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;
	
	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;
	
	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;
	
	@Transactional(readOnly = true)
	public List<PedidoResumoModel> buscarTodos(PedidoFilter filtro){
		List<Pedido> pedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));
		return pedidoResumoModelAssembler.toCollectionModel(pedidos);
	}
	
	@Transactional(readOnly = true)
	public PedidoModel buscar(String codigoPedido) {
		Pedido pedido = buscarOuFalhar(codigoPedido);
		return pedidoModelAssembler.toModel(pedido);
	}
	
	@Transactional
	public PedidoModel emitir(PedidoInput pedidoInput) {
		Pedido pedido = pedidoInputDisassembler.toDomainModel(pedidoInput);
		
		//Valida pedido:
		
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(pedido.getRestaurante().getId());
		FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(pedido.getFormaPagamento().getId());
		Cidade cidade = cadastroCidadeService.buscarOuFalhar(pedido.getEnderecoEntrega().getCidade().getId());
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(1L);
		
		pedido.getEnderecoEntrega().setCidade(cidade);
		pedido.setRestaurante(restaurante);
		pedido.setCliente(usuario);
		pedido.setFormaPagamento(formaPagamento);
		
		if(restaurante.naoAceitaFormaPagamento(formaPagamento)) {
			throw new NegocioException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.",
	                formaPagamento.getDescricao()));
		}
		
		// Valida itens:
		for(ItemPedido itemEntidade : pedido.getItens()) {
			//Uso do try-catch para lançar 400 na resposta ao invés de 404;
			try {
				Produto produto = cadastroProdutoService.buscarOuFalhar(pedidoInput.getRestaurante().getId(), itemEntidade.getProduto().getId());
				itemEntidade.setPedido(pedido);
				itemEntidade.setProduto(produto);
				itemEntidade.setPrecoUnitario(produto.getPreco());
				itemEntidade.setPrecoTotal(produto.getPreco().multiply(BigDecimal.valueOf(itemEntidade.getQuantidade())));
			} catch (EntidadeNaoEncontradaException e) {
				throw new NegocioException(e.getMessage());
			}
		}
		
		// Calculos:
		BigDecimal subTotal = BigDecimal.ZERO;
		for(ItemPedido item : pedido.getItens()) {
		    if (item.getPrecoUnitario() == null) {
		        item.setPrecoUnitario(BigDecimal.ZERO);
		    }

		    if (item.getQuantidade() == null) {
		        item.setQuantidade(0);
		    }
			subTotal = item.getPrecoTotal().add(subTotal);
		}
		pedido.setSubtotal(subTotal);
		pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
		pedido.setValorTotal(pedido.getSubtotal().add(pedido.getTaxaFrete()));
		
		pedido = pedidoRepository.save(pedido);
		return pedidoModelAssembler.toModel(pedido);
	}
	
	
	public Pedido buscarOuFalhar(String codigoPedido) {
		return pedidoRepository.findByCodigo(codigoPedido)
				.orElseThrow(() -> new PedidoNaoEncontradoException(codigoPedido));
	}
}
