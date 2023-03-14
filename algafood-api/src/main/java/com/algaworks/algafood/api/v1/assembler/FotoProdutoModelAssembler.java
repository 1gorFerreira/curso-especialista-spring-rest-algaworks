package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controllers.RestauranteProdutoFotoController;
import com.algaworks.algafood.api.v1.model.FotoProdutoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler extends RepresentationModelAssemblerSupport<FotoProduto, FotoProdutoModel>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity; 
	
	public FotoProdutoModelAssembler() {
		super(RestauranteProdutoFotoController.class, FotoProdutoModel.class);
	}
	
	@Override
	public FotoProdutoModel toModel(FotoProduto fotoProduto) {
		FotoProdutoModel fotoProdutoModel = modelMapper.map(fotoProduto, FotoProdutoModel.class);
        
		// Quem pode consultar restaurantes, também pode consultar os produtos e fotos
	    if (algaSecurity.podeConsultarRestaurantes()) {
	    	fotoProdutoModel.add(algaLinks.linkToFotoProduto(
	        		fotoProduto.getRestauranteId(), fotoProduto.getProduto().getId()));
	        
	        fotoProdutoModel.add(algaLinks.linkToProduto(
	        		fotoProduto.getRestauranteId(), fotoProduto.getProduto().getId(), "produto"));
	    }
		
		return fotoProdutoModel;
	}
	
}
