package com.algaworks.algafood.domain.v1.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.algaworks.algafood.core.validation.ValorZeroIncluiDescricao;

import lombok.Data;
import lombok.EqualsAndHashCode;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", 
	descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "restaurante")
public class Restaurante {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@NotBlank
	@Column(nullable = false)
	private String nome;
	
//	@NotNull 
//	@PositiveOrZero
	@Column(nullable = false)
	private BigDecimal taxaFrete;
	
//	@Valid // Com essa anotação, iremos validar as propriedades de COZINHA; 
//	@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)//Valida os atributos dentro de Cozinha que possuem o Group.CozinhaId;
//	@NotNull
	@ManyToOne // (fetch = FetchType.LAZY) O padrão das relações que terminam com ToOne é EAGER;
	@JoinColumn(name = "cozinha_id", nullable = false)
	private Cozinha cozinha;

	@Embedded
	private Endereco endereco;
	
	private Boolean ativo = Boolean.TRUE;
	
	private Boolean aberto = Boolean.FALSE;
	
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;
	
	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataAtualizacao;
	
	@ManyToMany // (fetch = FetchType.EAGER)  O padrão das relações que terminam com ToMany é LAZY; PS: Dificilmente usaremos EAGER em ToMany;
	@JoinTable(name = "restaurante_forma_pagamento",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private Set<FormaPagamento> formasPagamento = new HashSet<>();
	
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>(); 
	
	@ManyToMany 
	@JoinTable(name = "restaurante_usuario_responsavel",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn(name = "usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();
	
	public void ativar() {
		setAtivo(true);
	}
	
	public void inativar() {
		setAtivo(false);
	}
	
	public void abrir() {
		setAberto(true);
	}
	
	public void fechar() {
		setAberto(false);
	}
	
	public boolean adicionarFormaPagamento(FormaPagamento formaPagamento) {
		return formasPagamento.add(formaPagamento);
	}
	
	public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
		return formasPagamento.remove(formaPagamento);
	}
	
	public boolean associarResponsavel(Usuario responsavel) {
		return getResponsaveis().add(responsavel);
	}
	
	public boolean desassociarResponsavel(Usuario responsavel) {
		return getResponsaveis().remove(responsavel);
	}
	
	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
	    return getFormasPagamento().contains(formaPagamento);
	}

	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
	    return !aceitaFormaPagamento(formaPagamento);
	}
	
	public boolean isAberto() {
	    return this.aberto;
	}

	public boolean isFechado() {
	    return !isAberto();
	}

	public boolean isInativo() {
	    return !isAtivo();
	}

	public boolean isAtivo() {
	    return this.ativo;
	}

	public boolean aberturaPermitida() {
	    return isAtivo() && isFechado();
	}

	public boolean ativacaoPermitida() {
	    return isInativo();
	}

	public boolean inativacaoPermitida() {
	    return isAtivo();
	}

	public boolean fechamentoPermitido() {
	    return isAberto();
	}
}
