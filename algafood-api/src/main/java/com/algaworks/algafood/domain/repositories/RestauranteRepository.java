package com.algaworks.algafood.domain.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository
		extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante> {
	
//	Se um restaurante não tiver nenhuma forma de pg associada a ele, esse restaurante não será retornado.
//	Para resolver isso, temos que usar o LEFT JOIN FETCH, no lugar de JOIN FETCH;
	@Query("from Restaurante r join fetch r.cozinha") //left join fetch r.formasPagamento")
	List<Restaurante> findAll();

	List<Restaurante> queryByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

//	@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
	List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha);

//	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);

	Optional<Restaurante> findFirstByNomeContaining(String nome);

	List<Restaurante> findTop2ByNomeContaining(String nome);

	int countByCozinhaId(Long cozinha);
	
	boolean existsResponsavel(Long restauranteId, Long usuarioId);

//	Mesmo sem ter uma ligação explicita de RestauranteRepository com RestauranteRepositoryImpl,
//	o SpringDataJPA ao invés de tentar criar uma consulta pelo nome do método, orm.xml etc, ele vai
//	identificar que há uma implementação customizada e chamar esse método. (Para isso ser possível
//	é obrigatório que o sufixo seja (nome do repositorio) + Impl;
//	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

}
