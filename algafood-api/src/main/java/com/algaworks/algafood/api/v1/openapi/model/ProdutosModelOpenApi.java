package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.ProdutoModel;

import lombok.Data;

@Data
public class ProdutosModelOpenApi {

    private ProdutosEmbeddedModelOpenApi _embedded;
    private Links _links;
    
    @Data
    public class ProdutosEmbeddedModelOpenApi {
        
        private List<ProdutoModel> produtos;
        
    }    
}
