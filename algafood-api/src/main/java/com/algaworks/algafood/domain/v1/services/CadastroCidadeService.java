package com.algaworks.algafood.domain.v1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.v1.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.v1.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.v1.model.Cidade;
import com.algaworks.algafood.domain.v1.model.Estado;
import com.algaworks.algafood.domain.v1.repositories.CidadeRepository;

@Service
public class CadastroCidadeService {
	
	private static final String MSG_CIDADE_EM_USO 
    	= "Cidade de código %d não pode ser removida, pois está em uso";

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroEstadoService cadastroEstadoService;
	
	@Transactional(readOnly = true)
	public Cidade buscarOuFalhar(Long cidadeId) {
		Cidade cidade = cidadeRepository.findById(cidadeId)
				.orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
		return cidade;
	}

	@Transactional
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		
		Estado estado = cadastroEstadoService.buscarOuFalhar(estadoId);
				
		cidade.setEstado(estado);

		return cidadeRepository.save(cidade);
	}

	@Transactional
	public void excluir(Long cidadeId) {
		try {
			cidadeRepository.deleteById(cidadeId);
			cidadeRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(cidadeId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_CIDADE_EM_USO, cidadeId));
		}
	}
}
