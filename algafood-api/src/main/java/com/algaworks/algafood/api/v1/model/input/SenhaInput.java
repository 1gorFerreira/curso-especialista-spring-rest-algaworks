package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SenhaInput {
    
    @NotBlank
    private String senhaAtual;
    
    @NotBlank
    private String novaSenha;
    
}       
