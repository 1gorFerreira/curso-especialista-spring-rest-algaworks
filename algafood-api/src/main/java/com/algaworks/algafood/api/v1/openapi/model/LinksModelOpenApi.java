package com.algaworks.algafood.api.v1.openapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinksModelOpenApi {
	
	private LinkModel rel;

	@Getter
	@Setter
	private class LinkModel {
		
		private String href;
		private boolean templated;
		
	}
	
}
