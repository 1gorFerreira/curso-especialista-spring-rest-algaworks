package com.algaworks.algafood.core.security.authorizationserver;

import java.util.HashMap;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class JwtCustomClaimsTokenEnhancer implements TokenEnhancer{

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if(authentication.getPrincipal() instanceof AuthUser) { //Verificando se o fluxo do OAuth2 em uso utiliza autenticacao do usuario final;
			var authUser = (AuthUser) authentication.getPrincipal();
			
			//Se utiliza, iremos acrescentar informacoes ao Token:
			var info = new HashMap<String, Object>();
			info.put("nome_completo", authUser.getFullName());
			info.put("usuario_id", authUser.getUserId());
			
			//Downcast para acessar o metodo setAdditional... para adicionarmos as informacoes anteriores ao token;
			var oAuth2AccessToken = (DefaultOAuth2AccessToken) accessToken;
			oAuth2AccessToken.setAdditionalInformation(info);
			
		}
		
		return accessToken;
	}

	
}
