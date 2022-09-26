package com.algaworks.algafood.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repositories.CidadeRepository;
import com.algaworks.algafood.domain.repositories.EstadoRepository;

@Service
public class CadastroCidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estado = estadoRepository.buscar(estadoId);

		if (estado == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de estado com código %d", estadoId));
		}

		cidade.setEstado(estado);

		return cidadeRepository.salvar(cidade);
	}

	public void excluir(Long id) {
		try {
			cidadeRepository.remover(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException("Não existe um cadastro de cozinha de código: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					"Cozinha de código: " + id + " não pode ser removida, pois está em uso");
		}
	}
}
