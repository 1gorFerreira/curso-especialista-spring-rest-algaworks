package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.CozinhaModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CozinhasModelOpenApi {
	
	private CozinhasEmbeddedModelOpenApi _embedded;
	private Links _links;
	private PageModelOpenApi page;
	
	
	@Data
	public class CozinhasEmbeddedModelOpenApi {
		
		private List<CozinhaModel> cozinhas;
		
	}
	
}
