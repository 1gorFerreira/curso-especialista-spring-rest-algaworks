package com.algaworks.algafood.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient("algafood-web") // Acesso do client (Resource Owner) para autenticar no AuthorizationServer
			.secret(passwordEncoder.encode("web123"))
			.authorizedGrantTypes("password", "refresh_token")
			.scopes("write", "read")
			.accessTokenValiditySeconds(6 * 60 * 60) // 6 horas (Padrao e 12h)
			.refreshTokenValiditySeconds(60 * 24 * 60 * 60) // 60 dias
			
		.and()
			.withClient("foodanalytics") // Acesso do client (Alguma API) para autenticar no AuthorizationServer
			.secret(passwordEncoder.encode(""))
			.authorizedGrantTypes("authorization_code") // Aceita refresh token;
			.scopes("write", "read")
			.redirectUris("http://aplicacao-cliente")
		
		.and() // Nao recomendado
			.withClient("webadmin") // Acesso do client (Alguma API) para autenticar no AuthorizationServer
			.authorizedGrantTypes("implicit") // Nao aceita refresh token;
			.scopes("write", "read")
			.redirectUris("http://aplicacao-cliente")
			
		.and()
			.withClient("faturamento") // Acesso do client (Alguma API) para autenticar no AuthorizationServer
			.secret(passwordEncoder.encode("faturamento123"))
			.authorizedGrantTypes("client_credentials") // Nao aceita REFRESH TOKEN;
			.scopes("write", "read")
			
		.and()
			.withClient("checktoken") // Acesso proprio do ResourceServer para autenticar no AuthorizationServer;
			.secret(passwordEncoder.encode("check123"));
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService)
			.reuseRefreshTokens(false)
			.tokenGranter(tokenGranter(endpoints));
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.check TokenAccess("isAuthenticated()");
		security.checkTokenAccess("permitAll()")
		
// Ao inves de passar as credenciais do cliente apenas como HTTPBasic no Authorization, passaremos uma chave no corpo da req (x-www-form/form-data);
			.allowFormAuthenticationForClients();
	}
	
	
	// Parte da configuração do Authorization Server, para adicionar o TokenGranter customizado (PKCE)
	private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
		var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
				endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory());
		
		var granters = Arrays.asList(
				pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());
		
		return new CompositeTokenGranter(granters);
	}
	
}
