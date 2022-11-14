package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.EnderecoModel;
import com.algaworks.algafood.domain.model.Endereco;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		
		//Customizando a propriedade precoFrete dizendo que ele deve atribuir 
		//o valor encontrado em taxaFrete do modelo de dominio, devido que o
		//ModelMapper não conseguiu encontrar correspondencia:
		
//		modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
//			.addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);
	
		var enderecoToEnderecoModelTypeMapmodelMapper = modelMapper.createTypeMap(
				Endereco.class, EnderecoModel.class);
		
		//Essa implementação ficou diferente devido mapeamento mais complexo;
		enderecoToEnderecoModelTypeMapmodelMapper.<String>addMapping(
				enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(), 
				(enderecoModelDest, value) -> enderecoModelDest.getCidade().setEstado(value));
		
		return modelMapper;
	}
}
