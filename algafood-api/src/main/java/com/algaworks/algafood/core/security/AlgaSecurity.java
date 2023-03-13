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
	
	public boolean podeGerenciarPedidos(String codigoPedido) {
//		@PreAuthorize("hasAuthority('SCOPE_WRITE') and "
//				+ "(hasAuthority('GERENCIAR_PEDIDOS') or "
//				+ "@algaSecurity.gerenciaRestauranteDoPedido(#codigoPedido))")
		return hasAuthority("SCOPE_WRITE") && (hasAuthority("GERENCIAR_PEDIDOS")
				|| gerenciaRestauranteDoPedido(codigoPedido));
	}
	
}
