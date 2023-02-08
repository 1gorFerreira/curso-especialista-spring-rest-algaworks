package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controllers.RestauranteController;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	public RestauranteModelAssembler() {
		super(RestauranteController.class, RestauranteModel.class);
	}
	
	@Override
	public RestauranteModel toModel(Restaurante restaurante) {
		RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
		modelMapper.map(restaurante, restauranteModel);
		
		restauranteModel.add(algaLinks.linkToRestaurantes("restaurantes"));
		
		restauranteModel.add(algaLinks.linkToRestauranteFormasPagamento(restauranteModel.getId(), "formas-pagamento"));
		
		restauranteModel.add(algaLinks.linkToResponsaveisRestaurante(restauranteModel.getId(), "responsaveis"));
		
		if(restaurante.ativacaoPermitida()) {
			restauranteModel.add(algaLinks.linkToRestauranteAtivacao(restauranteModel.getId(), "ativar"));
		}
		
		if(restaurante.inativacaoPermitida()) {
			restauranteModel.add(algaLinks.linkToRestauranteInativacao(restauranteModel.getId(), "inativar"));
		}
		
		if(restaurante.aberturaPermitida()) {
			restauranteModel.add(algaLinks.linkToRestauranteAbertura(restauranteModel.getId(), "abrir"));
		}
		
		if(restaurante.fechamentoPermitido()) {
			restauranteModel.add(algaLinks.linkToRestauranteFechamento(restauranteModel.getId(), "fechar"));
		}
		
		restauranteModel.getCozinha().add(algaLinks.linkToCozinha(restauranteModel.getCozinha().getId()));
		
		restauranteModel.getEndereco().getCidade().add(algaLinks.linkToCidade(restauranteModel.getEndereco().getCidade().getId()));
		
		return restauranteModel;
	}
	
	@Override
	public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> restaurantes){
		return super.toCollectionModel(restaurantes)
				.add(algaLinks.linkToRestaurantes());
	}
	
}
