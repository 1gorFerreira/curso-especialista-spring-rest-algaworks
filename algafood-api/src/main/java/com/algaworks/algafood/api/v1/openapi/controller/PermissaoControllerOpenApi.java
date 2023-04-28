package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.PermissaoModel;

@SecurityRequirement(name = "security_auth")
public interface PermissaoControllerOpenApi {

    CollectionModel<PermissaoModel> listar();
    
}
