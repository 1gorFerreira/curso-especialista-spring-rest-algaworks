package com.algaworks.algafood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.PedidoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.domain.model.filter.PedidoFilter;

@SecurityRequirement(name = "security_auth")
public interface PedidoControllerOpenApi {

	ResponseEntity<PagedModel<PedidoResumoModel>> pesquisar(PedidoFilter filtro, Pageable pageable);

	ResponseEntity<PedidoModel> buscar(String codigoPedido);

	ResponseEntity<PedidoModel> emitir(PedidoInput pedidoInput);

}