package com.algaworks.algafood.domain.v1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.v1.model.FotoProduto;
import com.algaworks.algafood.domain.v1.model.Produto;
import com.algaworks.algafood.domain.v1.model.Restaurante;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryQueries{
	
	@Query("from Produto where restaurante.id = :restaurante and id = :produto")
    Optional<Produto> findById(@Param("restaurante") Long restauranteId, 
            @Param("produto") Long produtoId);
	
	List<Produto> findTodosByRestaurante(Restaurante restaurante);
	
	@Query("from Produto p where p.ativo=true and p.restaurante= :restaurante")
	List<Produto> findAtivosByRestaurante(Restaurante restaurante);
	
	@Query("from FotoProduto fp join fp.produto p "
			+ "where p.restaurante.id= :restauranteId and fp.produto.id= :produtoId")
	Optional<FotoProduto> findFotoById(Long restauranteId, Long produtoId);
	
}
