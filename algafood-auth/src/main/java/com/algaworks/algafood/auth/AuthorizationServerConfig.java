package com.algaworks.algafood.auth;

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
			.withClient("faturamento") // Acesso do client (Alguma API) para autenticar no AuthorizationServer
			.secret(passwordEncoder.encode("faturamento123"))
			.authorizedGrantTypes("client_credentials")
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
			.reuseRefreshTokens(false);
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.check TokenAccess("isAuthenticated()");
		security.checkTokenAccess("permitAll()");
	}
	
}
