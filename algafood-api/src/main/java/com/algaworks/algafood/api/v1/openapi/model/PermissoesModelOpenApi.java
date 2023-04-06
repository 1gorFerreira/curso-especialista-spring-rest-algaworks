package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.PermissaoModel;

import lombok.Data;

@Data
public class PermissoesModelOpenApi {

    private PermissoesEmbeddedModelOpenApi _embedded;
    private Links _links;
    
    @Data
    public class PermissoesEmbeddedModelOpenApi {
        
        private List<PermissaoModel> permissoes;
        
    }   
}
