package com.algaworks.algafood.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.repositories.PedidoRepository;
import com.algaworks.algafood.domain.repositories.RestauranteRepository;

@Component
public class AlgaSecurity {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public boolean isAutenticado() {
	    return getAuthentication().isAuthenticated();
	}
	
	public Long getUsuarioId() {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();

		return jwt.getClaim("usuario_id");
	}
	
	public String getUsuarioToken() {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();
		
		return jwt.getTokenValue();
	}
	
	public boolean gerenciaRestaurante(Long restauranteId) {
		if(restauranteId == null) {
			return false;
		}
		return restauranteRepository.existsResponsavel(restauranteId, getUsuarioId());
	}
	
	public boolean gerenciaRestauranteDoPedido(String codigoPedido) {
		if(codigoPedido == null) {
			return false;
		}
		return pedidoRepository.isPedidoGerenciadoPor(codigoPedido, getUsuarioId());
	}
	
	public boolean usuarioAutenticadoIgual(Long usuarioId) {
		return getUsuarioId() != null && usuarioId != null && getUsuarioId().equals(usuarioId);
	}
	
	public boolean hasAuthority(String authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName)); // condicao que retorna true or false;
	}
	
	public boolean temEscopoEscrita() {
	    return hasAuthority("SCOPE_WRITE");
	}

	public boolean temEscopoLeitura() {
	    return hasAuthority("SCOPE_READ");
	}
	
	public boolean podeGerenciarPedidos(String codigoPedido) {
//		@PreAuthorize("hasAuthority('SCOPE_WRITE') and "
//				+ "(hasAuthority('GERENCIAR_PEDIDOS') or "
//				+ "@algaSecurity.gerenciaRestauranteDoPedido(#codigoPedido))")
		return hasAuthority("SCOPE_WRITE") && (hasAuthority("GERENCIAR_PEDIDOS")
				|| gerenciaRestauranteDoPedido(codigoPedido));
	}
	
	public boolean podeConsultarRestaurantes() {
//		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
	    return temEscopoLeitura() && isAutenticado();
	}

	public boolean podeGerenciarCadastroRestaurantes() {
//		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_RESTAURANTE'))"
	    return temEscopoEscrita() && hasAuthority("EDITAR_RESTAURANTES");
	}

	public boolean podeGerenciarFuncionamentoRestaurantes(Long restauranteId) {
//		@PreAuthorize("hasAuthority('SCOPE_WRITE') and "
//				+ "(hasAuthority('EDITAR_RESTAURANTES') or "
//				+ "@algaSecurity.gerenciaRestaurante(#restauranteId)))"
	    return temEscopoEscrita() && (hasAuthority("EDITAR_RESTAURANTES")
	            || gerenciaRestaurante(restauranteId));
	}

	public boolean podeConsultarUsuariosGruposPermissoes() {
//		@PreAuthorize("hasAuthority('SCOPE_READ') and hasAuthority('CONSULTAR_USUARIOS_GRUPOS_PERMISSOES')")
	    return temEscopoLeitura() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
	}

	public boolean podeEditarUsuariosGruposPermissoes() {
//		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES')")
	    return temEscopoEscrita() && hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES");
	}

	public boolean podePesquisarPedidos(Long clienteId, Long restauranteId) {
//		@PreAuthorize("hasAuthority('SCOPE_READ') and "
//				+ "(hasAuthority('CONSULTAR_PEDIDOS') or "
//				+ "@algaSecurity.usuarioAutenticadoIgual(#filtro.clienteId) or "
//				+ "@algaSecurity.gerenciaRestaurante(#filtro.restauranteId))")
	    return temEscopoLeitura() && (hasAuthority("CONSULTAR_PEDIDOS")
	            || usuarioAutenticadoIgual(clienteId) || gerenciaRestaurante(restauranteId));
	}

	public boolean podePesquisarPedidos() {
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarFormasPagamento() {
//		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarCidades() {
//		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarEstados() {
//		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarCozinhas() {
//		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
	    return isAutenticado() && temEscopoLeitura();
	}

	public boolean podeConsultarEstatisticas() {
//		@PreAuthorize("hasAuthority('SCOPE_READ') and hasAuthority('GERAR_RELATORIOS')")
	    return temEscopoLeitura() && hasAuthority("GERAR_RELATORIOS");
	}    
	
}
