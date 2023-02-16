package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.domain.v1.model.Cidade;
import com.algaworks.algafood.domain.v1.model.Cozinha;
import com.algaworks.algafood.domain.v1.model.Restaurante;

@Component
public class RestauranteInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	//Esse método instancia um novo objeto Restaurante, copiando as propriedades do input;
	public Restaurante toDomainObject(RestauranteInput restauranteInput) {
		return modelMapper.map(restauranteInput, Restaurante.class);
	}
	
	//Esse método não vai instanciar um novo objeto Restaurante, apenas copiar atributos de um para outro;
	public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
		// Para evitar org.hibernate.HbernateException: identifier of an instance of
		// com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2;
		restaurante.setCozinha(new Cozinha());
		
		if(restaurante.getEndereco() != null) {
			restaurante.getEndereco().setCidade(new Cidade());
		}
		
		modelMapper.map(restauranteInput, restaurante);
	}
	
}
