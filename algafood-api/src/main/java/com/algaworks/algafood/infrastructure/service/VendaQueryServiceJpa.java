package com.algaworks.algafood.infrastructure.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.model.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.services.VendaQueryService;

@Repository
public class VendaQueryServiceJpa implements VendaQueryService{
	
	/* SELECT DATE(p.data_criacao) as data_criacao,
	 * 		count(p.id) as total_vendas,
	 * 		sum(p.valor_total) as total_faturado
	 * FROM pedido p
	 * 
	 * Predicates (Filtros);
	 * 
	 * where p.status in ('CONFIRMADO', 'ENTREGUE')
	 * group by date(p.data_criacao)
	 * 
	 * PS: Como armazenamos no BD as datas com o UTC, quando fizermos essa consulta
	 * e caso possua uma data como por exemplo 2019-11-03 02:00:00 ela irá contabilizar
	 * como uma venda no dia 03, quando na verdade caso ela tenha sido feita no horário de
	 * brasilia, essa compra seria do dia 02 as 23H.
	 * --------------------------------------------
	 * 
	 * SELECT DATE(convert_tz(p.data_criacao, '+00:00', '-03:00')) as data_criacao,
	 * 		count(p.id) as total_vendas,
	 * 		sum(p.valor_total) as total_faturado
	 * FROM pedido p
	 * 
	 * Predicates (Filtros);
	 * 
	 * where p.status in ('CONFIRMADO', 'ENTREGUE')
	 * group by date(convert_tz(p.data_criacao, '+00:00', '-03:00'))
	 * 
	 * PS2: O convertTz resolve o problema citado no PS anterior, ele recebe 3 parâmetros:
	 * A prórpria data,
	 * O OffSet (Nesse caso nosso offSet é UTC, então 00:00) e para qual
	 * OffSet queremos ir (Nesse caso, fusohorario de Brasilia -03:00);
	 */

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset){
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class);
		var root = query.from(Pedido.class);
		var predicates = new ArrayList<Predicate>();
		
		// Parâmetros: Função no BD, Tipo esperado ao executar a função, argumentos (Nesse caso os args serão os PS2);
		var functionConvertTzDataCriacao = builder.function("convert_tz", Date.class, root.get("dataCriacao"), builder.literal("+00:00"), builder.literal(timeOffset));
		
		// Parâmetros: Função no BD, Tipo esperado ao executar a função, argumentos
		var functionDateDataCriacao = builder.function("date", Date.class, functionConvertTzDataCriacao);
		
		var selection = builder.construct(VendaDiaria.class, // Chama o construtor de VendaDiaria;
				functionDateDataCriacao, 
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal")));
		
		if(filtro.getRestauranteId() != null) {
			predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
		}
		
		if(filtro.getDataCriacaoInicio() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
		}
		
		if (filtro.getDataCriacaoFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
		}
		
		predicates.add(root.get("status").in(
				StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));
		
		query.select(selection);
		query.where(predicates.toArray(new Predicate[0]));
		query.groupBy(functionDateDataCriacao);
		
		return manager.createQuery(query).getResultList(); // getResultList() Retorna um List de VendaDiaria;
	}
}
