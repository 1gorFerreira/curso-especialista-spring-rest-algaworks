package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repositories.GrupoRepository;

@Service
public class CadastroGrupoService {
	
	private static final String MSG_GRUPO_EM_USO 
    = "Grupo de código %d não pode ser removido, pois está em uso";

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@Autowired
	private GrupoInputDisassembler grupoInputDisassembler;
	
	@Transactional(readOnly = true)
	public List<GrupoModel> buscarTodos() {
		List<Grupo> grupos = grupoRepository.findAll();
		return grupoModelAssembler.toCollectionModel(grupos);
	}
	
	@Transactional(readOnly = true)
	public GrupoModel buscar(Long grupoId){
		Grupo grupo = grupoRepository.findById(grupoId)
				.orElseThrow(() -> new GrupoNaoEncontradoException(grupoId));
		return grupoModelAssembler.toModel(grupo);
	}
	
	@Transactional
	public GrupoModel adicionar(GrupoInput grupoInput){
		Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
		grupo = grupoRepository.save(grupo);
		return grupoModelAssembler.toModel(grupo);
	}
	
	@Transactional
	public GrupoModel atualizar(Long grupoId, GrupoInput grupoInput){
		Grupo grupo = grupoRepository.findById(grupoId)
				.orElseThrow(() -> new GrupoNaoEncontradoException(grupoId));
		
		grupoInputDisassembler.copyToDomainObject(grupoInput, grupo);
		
		grupo = grupoRepository.save(grupo);
		
		return grupoModelAssembler.toModel(grupo);
	}
	
	public void deletar (Long grupoId) {
		try {
			grupoRepository.deleteById(grupoId);
		} catch (EmptyResultDataAccessException e) {
            throw new GrupoNaoEncontradoException(grupoId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_GRUPO_EM_USO, grupoId));
        }
	}
}
