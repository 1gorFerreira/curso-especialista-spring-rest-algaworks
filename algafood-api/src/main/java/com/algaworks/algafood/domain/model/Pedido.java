package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.algaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.exception.NegocioException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "pedido")
public class Pedido extends AbstractAggregateRoot<Pedido>{

	@EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String codigo;
	
	private BigDecimal subtotal;
	
	private BigDecimal taxaFrete;
	
	private BigDecimal valorTotal;
	
	@CreationTimestamp
    private OffsetDateTime dataCriacao;
	
	private OffsetDateTime dataConfirmacao;
	
	private OffsetDateTime dataCancelamento;
	
	private OffsetDateTime dataEntrega;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Restaurante restaurante;
	
	@ManyToOne
	@JoinColumn(name = "usuario_cliente_id", nullable = false)
	private Usuario cliente;
	
	@ManyToOne(fetch = FetchType.LAZY) //Só faz o Select se eu precisar;
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
	@Embedded
    private Endereco enderecoEntrega;
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.CRIADO;
	
	@OneToMany(mappedBy ="pedido", cascade = CascadeType.ALL)//Cascade vai forçar que os os itens de pedido também sejam salvos (Salva em cascata);
	private List<ItemPedido> itens = new ArrayList<>();
	
	
	public void confirmar() {
		setStatus(StatusPedido.CONFIRMADO);
		setDataConfirmacao(OffsetDateTime.now());
		
		registerEvent(new PedidoConfirmadoEvent(this));
	}
	
	public void entregar() {
		setStatus(StatusPedido.ENTREGUE);
		setDataEntrega(OffsetDateTime.now());
		
		registerEvent(new PedidoCanceladoEvent(this));
	}
	
	public void cancelar() {
		setStatus(StatusPedido.CANCELADO);
		setDataCancelamento(OffsetDateTime.now());
	}
	
	private void setStatus(StatusPedido novoStatus) {
		if(getStatus().naoPodeAlterarPara(novoStatus)) {
			throw new NegocioException(String.format("Status do pedido %s não pode ser alterado de %s para %s",
				getCodigo(), getStatus().getDescricao(), novoStatus.getDescricao()));
		}
		this.status = novoStatus;
	}
	
	public boolean podeSerConfirmado() {
		return getStatus().podeAlterarPara(StatusPedido.CONFIRMADO);
	}
	
	public boolean podeSerEntregue() {
		return getStatus().podeAlterarPara(StatusPedido.ENTREGUE);
	}
	
	public boolean podeSerCancelado() {
		return getStatus().podeAlterarPara(StatusPedido.CANCELADO);
	}
	
	@PrePersist // Método de callBack do JPA;
	private void gerarCodigo() {
		setCodigo(UUID.randomUUID().toString());
	}
	
}
