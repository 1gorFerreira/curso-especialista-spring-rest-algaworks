package com.algaworks.algafood.domain.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.api.v1.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.model.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
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
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional(readOnly = true)
	public CollectionModel<UsuarioModel> buscarTodos() {
		List<Usuario> todosUsuarios = usuarioRepository.findAll();
		
		return usuarioModelAssembler.toCollectionModel(todosUsuarios);
	}
	
	@Transactional(readOnly = true)
	public UsuarioModel buscar(Long usuarioId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@Transactional
	public UsuarioModel adicionar(UsuarioComSenhaInput usuarioComSenhaInput) {
		Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioComSenhaInput);
		
		regraEmail(usuario);
		
		if(usuario.isNovo()) {
			usuario.setSenha(passwordEncoder.encode(usuarioComSenhaInput.getSenha()));
		}
		
		usuario = usuarioRepository.save(usuario);
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@Transactional
	public UsuarioModel atualizar(Long usuarioId, UsuarioInput usuarioInput) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		
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
		Usuario usuario = buscarOuFalhar(usuarioId);
		
		if(!passwordEncoder.matches(senhaInput.getSenhaAtual(), usuario.getSenha())) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		}
		
		usuario.setSenha(passwordEncoder.encode(senhaInput.getNovaSenha()));
	}
	
	@Transactional
	public CollectionModel<GrupoModel> listarGrupos(Long usuarioId){
		Usuario usuario = buscarOuFalhar(usuarioId);
		return grupoModelAssembler.toCollectionModel(usuario.getGrupos());
	}
	
	@Transactional
	public void associarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
		
		usuario.associarGrupo(grupo);
	}
	
	@Transactional
	public void desassociarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
		
		usuario.desassociarGrupo(grupo);
	}
	
	
	@Transactional
	public Usuario buscarOuFalhar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
		.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
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
