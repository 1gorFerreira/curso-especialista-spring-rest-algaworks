package com.algaworks.algafood.domain.v1.repositories;

import java.math.BigDecimal;
import java.util.List;

import com.algaworks.algafood.domain.v1.model.Restaurante;

//Essa interface busca criar um vinculo entre RestauranteRepository com RestauranteRepositoryImpl,
//pois caso mude o nome/método na classe RestauranteRepositoryImpl, não precisaremos refatorar o método
//diretamente dentro do RestauranteRepository, já que RestauranteRepository herda os métodos dessa interface 
//que consequentemente tem sua implementação em RestauranteRepositoryImpl. Resolvendo o problema da PT1;
//RestauranteRepositoryImpl -> RestauranteRepositoryQueries -> RestauranteRepository;
public interface RestauranteRepositoryQueries {

	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);
	
	List<Restaurante> findComFreteGratis(String nome);

}