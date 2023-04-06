package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.GrupoModel;

import lombok.Data;

@Data
public class GruposModelOpenApi {

    private GruposEmbeddedModelOpenApi _embedded;
    private Links _links;
    
    @Data
    public class GruposEmbeddedModelOpenApi {
        
        private List<GrupoModel> grupos;
        
    }   
}
