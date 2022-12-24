package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

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
		if(!getStatus().naoPodeAlterarPara(novoStatus)) {
			throw new NegocioException(String.format("Status do pedido %d não pode ser alterado de %s para %s",
				getCodigo(), getStatus().getDescricao(), novoStatus.getDescricao()));
		}
		this.status = novoStatus;
	}
	
	@PrePersist // Método de callBack do JPA;
	private void gerarCodigo() {
		setCodigo(UUID.randomUUID().toString());
	}
	
}
