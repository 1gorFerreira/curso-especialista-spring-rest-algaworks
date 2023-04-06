package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageableModelOpenApi {
	
	private int page;
	
	private int size;
	
	private List<String> sort;
	
}
