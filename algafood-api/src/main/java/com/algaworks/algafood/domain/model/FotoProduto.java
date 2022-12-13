package com.algaworks.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class FotoProduto {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "produto_id")
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY) // Na maioria das vezes que buscarmos produto, não precisaremos da fotoProduto;
    @MapsId // Estamos dizendo que a entidade produto é mapeada através do id da entidade FotoProduto (produto_id nesse caso);
    private Produto produto;
    
    private String nomeArquivo;
    private String descricao;
    private String contentType;
    private Long tamanho;
    
}