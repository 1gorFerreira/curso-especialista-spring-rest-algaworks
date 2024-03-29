package com.algaworks.algafood.domain.model;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Id;

public enum StatusPedido {

    CRIADO("Criado"),
    CONFIRMADO("Confirmado", CRIADO),
    ENTREGUE("Entregue", CONFIRMADO),
    CANCELADO("Cancelado", CRIADO);
    
    private String descricao;
    private List<StatusPedido> statusAnteriores; //Status permitidos para chegar no próximo;
    
    //com o varargs podemos passar nenhum, um ou vários status;
    StatusPedido(String descricao, StatusPedido... statusAnteriores) {
    	this.descricao = descricao;
    	this.statusAnteriores = Arrays.asList(statusAnteriores);
    }
    
    public String getDescricao() {
    	return this.descricao;
    }
    
    public boolean naoPodeAlterarPara(StatusPedido novoStatus) {
    	return !novoStatus.statusAnteriores.contains(this);
    }
    
    public boolean podeAlterarPara(StatusPedido novoStatus) {
    	return !naoPodeAlterarPara(novoStatus);
    }
}