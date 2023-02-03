package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controllers.CozinhaController;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaModelAssembler extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CozinhaModelAssembler() {
		super(CozinhaController.class, CozinhaModel.class);
	}
	
	@Override
	public CozinhaModel toModel(Cozinha cozinha) {
		CozinhaModel cozinhaModel = modelMapper.map(cozinha, CozinhaModel.class);
		
		cozinhaModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CozinhaController.class)
				.buscar(cozinhaModel.getId())).withSelfRel());
		
		cozinhaModel.add(WebMvcLinkBuilder.linkTo(CozinhaController.class).withRel("cozinhas"));
		
		return cozinhaModel;
	}

}
