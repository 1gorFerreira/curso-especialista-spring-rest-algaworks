package com.algaworks.algafood.domain.v1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.v1.exception.PermissaoNaoEncontradaException;
import com.algaworks.algafood.domain.v1.model.Permissao;
import com.algaworks.algafood.domain.v1.repositories.PermissaoRepository;

@Service
public class CadastroPermissaoService {

	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Transactional
	public Permissao buscarOuFalhar(Long permissaoId) {
		Permissao permissao = permissaoRepository.findById(permissaoId)
				.orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
		return permissao;
	}
}
