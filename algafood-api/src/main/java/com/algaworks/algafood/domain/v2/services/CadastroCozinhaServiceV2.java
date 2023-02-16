package com.algaworks.algafood.domain.v2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.v2.assembler.CozinhaInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CozinhaModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.domain.v1.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.v1.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.v1.model.Cozinha;
import com.algaworks.algafood.domain.v1.repositories.CozinhaRepository;

@Service
public class CadastroCozinhaServiceV2 {
	
	private static final String MSG_COZINHA_EM_USO
	= "Cozinha de código %d não pode ser removida, pois está em uso";
	
	@Autowired
	private CozinhaModelAssemblerV2 cozinhaModelAssembler;
	
	@Autowired
	private CozinhaInputDisassemblerV2 cozinhaInputDisassembler;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
	@Transactional(readOnly = true)
	public PagedModel<CozinhaModelV2> listar(Pageable pageable) {
		Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
		
//		List<CozinhaModel> cozinhasModel = cozinhaModelAssembler.toCollectionModel(cozinhasPage.getContent());
//		
//		Page<CozinhaModel> cozinhasModelPage = new PageImpl<>(cozinhasModel, pageable, cozinhasPage.getTotalElements());
		
		PagedModel<CozinhaModelV2> cozinhasPagedModel = pagedResourcesAssembler.toModel(cozinhasPage, cozinhaModelAssembler);
		
		return cozinhasPagedModel;
	}
	
	@Transactional(readOnly = true)
	public CozinhaModelV2 buscar(Long cozinhaId) {
		Cozinha cozinha = buscarOuFalhar(cozinhaId);
		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	@Transactional
	public CozinhaModelV2 adicionar(CozinhaInputV2 cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		
		cozinha = cozinhaRepository.save(cozinha);
		
		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	@Transactional
	public CozinhaModelV2 atualizar(Long cozinhaId, CozinhaInputV2 cozinhaInput) {
		Cozinha cozinha = buscarOuFalhar(cozinhaId);
		
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinha);
		
		cozinha = cozinhaRepository.save(cozinha);
		
		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	@Transactional
	public void excluir(Long cozinhaId) {
		try {
			cozinhaRepository.deleteById(cozinhaId);
			cozinhaRepository.flush();
			
		}catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(cozinhaId);
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_COZINHA_EM_USO, cozinhaId));
		}
	}
	
	@Transactional(readOnly = true)
	public Cozinha buscarOuFalhar(Long cozinhaId) {
		return cozinhaRepository.findById(cozinhaId).orElseThrow(
				() -> new CozinhaNaoEncontradaException(cozinhaId));
	}
}
