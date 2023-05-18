package com.algaworks.algafood.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

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
    
    public Long getRestauranteId() {
		if (getProduto() != null) {
			return getProduto().getRestaurante().getId();
		}
		
		return null;
	}
    
}