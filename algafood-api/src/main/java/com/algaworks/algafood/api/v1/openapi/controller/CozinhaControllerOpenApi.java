package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;

public interface CozinhaControllerOpenApi {

    ResponseEntity<PagedModel<CozinhaModel>> listar(Pageable pageable);
    
    ResponseEntity<CozinhaModel> buscar(Long cozinhaId);
    
    CozinhaModel adicionar(CozinhaInput cozinhaInput);
    
    ResponseEntity<CozinhaModel> atualizar(Long cozinhaId, CozinhaInput cozinhaInput);
    
    void remover(Long cozinhaId);   
}        
