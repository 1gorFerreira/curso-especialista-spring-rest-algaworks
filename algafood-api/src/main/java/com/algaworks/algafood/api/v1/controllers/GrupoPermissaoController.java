package com.algaworks.algafood.api.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.api.v1.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.algaworks.algafood.domain.v1.services.CadastroGrupoService;

@RestController
@RequestMapping(path = "/v1/grupos/{grupoId}/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi{

	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@GetMapping
	public ResponseEntity<CollectionModel<PermissaoModel>> listar(@PathVariable Long grupoId){
		CollectionModel<PermissaoModel> permissoes = cadastroGrupoService.listarPermissoes(grupoId);
		
        permissoes.removeLinks()
                .add(algaLinks.linkToGrupoPermissoes(grupoId))
                .add(algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));
        
        permissoes.getContent().forEach(permissaoModel -> {
            permissaoModel.add(algaLinks.linkToGrupoPermissaoDesassociacao(
                    grupoId, permissaoModel.getId(), "desassociar"));
        });

		return ResponseEntity.ok(permissoes);
	}
	
	@PutMapping("/{permissaoId}")
	public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
		cadastroGrupoService.associarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{permissaoId}")
	public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
		cadastroGrupoService.desassociarPermissao(grupoId, permissaoId);
		return ResponseEntity.noContent().build();
	}

}
