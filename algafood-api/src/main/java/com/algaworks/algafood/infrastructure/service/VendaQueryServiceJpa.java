package com.algaworks.algafood.infrastructure.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.model.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.services.VendaQueryService;

@Repository
public class VendaQueryServiceJpa implements VendaQueryService{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro){
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class);
		var root = query.from(Pedido.class);
		
		// Parâmetros: Função no BD, Tipo esperado ao executar a função, argumentos
		var functionDateDataCriacao = builder.function("date", Date.class, root.get("dataCriacao"));
		
		var selection = builder.construct(VendaDiaria.class, // Chama o construtor de VendaDiaria;
				functionDateDataCriacao, 
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal")));
		
		query.select(selection);
		query.groupBy(functionDateDataCriacao);
		
		return manager.createQuery(query).getResultList(); // getResultList() Retorna um List de VendaDiaria;
	}
}
