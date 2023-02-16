package com.algaworks.algafood.domain.v1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.v1.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.v1.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.v1.model.Estado;
import com.algaworks.algafood.domain.v1.repositories.EstadoRepository;

@Service
public class CadastroEstadoService {
	
	private static final String MSG_ESTADO_EM_USO  = 
	        "Estado de código %d não pode ser removido, pois está em uso";

    @Autowired
    private EstadoRepository estadoRepository;
    
    @Transactional(readOnly = true)
    public Estado buscarOuFalhar(Long estadoId) {
    	Estado estado = estadoRepository.findById(estadoId)
    			.orElseThrow(() -> new EstadoNaoEncontradoException((estadoId)));
    	return estado;
    }
    
    @Transactional
    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }
    
    @Transactional
    public void excluir(Long estadoId) {
        try {
            estadoRepository.deleteById(estadoId);
            estadoRepository.flush();
            
        } catch (EmptyResultDataAccessException e) {
            throw new EstadoNaoEncontradoException(estadoId);
        
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_ESTADO_EM_USO, estadoId));
        }
    }
    
}
