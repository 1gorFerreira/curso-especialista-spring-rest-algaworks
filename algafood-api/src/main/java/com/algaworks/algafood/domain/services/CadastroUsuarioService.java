package com.algaworks.algafood.domain.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repositories.UsuarioRepository;

@Service
public class CadastroUsuarioService {
	
	private static final String MSG_USUARIO_EM_USO 
    = "Usuário de código %d não pode ser removido, pois está em uso";

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private UsuarioInputDisassembler usuarioInputDisassembler;
	
	@Transactional(readOnly = true)
	public List<UsuarioModel> buscarTodos() {
		return usuarioModelAssembler.toCollectionModel(usuarioRepository.findAll());
	}
	
	@Transactional(readOnly = true)
	public UsuarioModel buscar(Long usuarioId) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@Transactional
	public UsuarioModel adicionar(UsuarioComSenhaInput usuarioComSenhaInput) {
		Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioComSenhaInput);
		
		regraEmail(usuario);
		
		usuario = usuarioRepository.save(usuario);
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@Transactional
	public UsuarioModel atualizar(Long usuarioId, UsuarioInput usuarioInput) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
		
		usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuario);
		
		regraEmail(usuario);
		
		usuario = usuarioRepository.save(usuario);
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@Transactional
	public void deletar(Long usuarioId) {
		try {
			usuarioRepository.deleteById(usuarioId);
			usuarioRepository.flush();
		}  catch (EmptyResultDataAccessException e) {
            throw new GrupoNaoEncontradoException(usuarioId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_USUARIO_EM_USO, usuarioId));
        }
	}
	
	@Transactional
	public void atualizarSenha(Long usuarioId, SenhaInput senhaInput) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
		
		if(usuario.senhaNaoCoincideCom(senhaInput.getSenhaAtual())) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		}
		
		usuario.setSenha(senhaInput.getNovaSenha());
	}
	
	
	private void regraEmail(Usuario usuario) {
		usuarioRepository.detach(usuario);
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		
		if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException(
					String.format("Já existe um usuário cadastrado com e-mail %s", usuario.getEmail()));
		}
	}
}
