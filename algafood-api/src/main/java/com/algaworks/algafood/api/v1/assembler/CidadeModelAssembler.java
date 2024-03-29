package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controllers.CidadeController;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity; 
	
	public CidadeModelAssembler() {
		super(CidadeController.class, CidadeModel.class);
	}
	
	@Override
	public CidadeModel toModel(Cidade cidade) {
//		O Spring HATEOAS possui esse metodo createModelWithId que ja cria com os links SELF;
		CidadeModel cidadeModel = createModelWithId(cidade.getId(), cidade);
		
//		Mas ainda assim precisamos do modelMapper para fazer as conversoes;
		modelMapper.map(cidade, cidadeModel);

		if (algaSecurity.podeConsultarCidades()) {
			cidadeModel.add(algaLinks.linkToCidades("cidades"));
		}
		
		if (algaSecurity.podeConsultarEstados()) {
			cidadeModel.getEstado().add(algaLinks.linkToEstado(cidadeModel.getEstado().getId()));
		}
		
		return cidadeModel;
	}
	
	@Override
	public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> cidades) {
		CollectionModel<CidadeModel> collectionModel = super.toCollectionModel(cidades);
	    
	    if (algaSecurity.podeConsultarCidades()) {
	        collectionModel.add(algaLinks.linkToCidades());
	    }
	    
	    return collectionModel;
	}
	
//	public List<CidadeModel> toCollectionModel(List<Cidade> cidades){
//		return cidades.stream()
//			.map(cidade -> toModel(cidade)).toList();
//	}
	
}
