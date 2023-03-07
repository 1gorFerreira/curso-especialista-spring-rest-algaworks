package com.algaworks.algafood.auth.core;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtKeyStoreProperties jwtKeyStoreProperties;
	
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
			.redirectUris("http://localhost:8082")
		
		.and() // Nao recomendado
			.withClient("webad min") // Acesso do client (Alguma API) para autenticar no AuthorizationServer
			.authorizedGrantTypes("implicit") // Nao aceita refresh token;
			.scopes("write", "read")
			.redirectUris("http://localhost:8082")
			
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
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.check TokenAccess("isAuthenticated()");
		security.checkTokenAccess("permitAll()")
			.tokenKeyAccess("permitAll()") // Liberando acesso para o endpoint de oauth/token_key para resgate da chave publica;
		
// Ao inves de passar as credenciais do cliente apenas como HTTPBasic no Authorization, passaremos uma chave no corpo da req (x-www-form/form-data);
			.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService)
			.reuseRefreshTokens(false)
			.accessTokenConverter(jwtAccessTokenConverter())
			.approvalStore(approvalStore(endpoints.getTokenStore()))
			.tokenGranter(tokenGranter(endpoints));
	}
	
//  Atribuindo ao approvalStore um tokenApprovalStore que vai permitir que o AuthorizationServer permita aprovacao granular dos escopos (write, read, etc)
	private ApprovalStore approvalStore(TokenStore tokenStore) {
		var approvalStore = new TokenApprovalStore();
		approvalStore.setTokenStore(tokenStore);
		
		return approvalStore;
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//		jwtAccessTokenConverter.setSigningKey("89asjkdhiuhep121289u3hdpasuighd1he9"); Chave simetrica

		var jksResource = new ClassPathResource(jwtKeyStoreProperties.getPath());
		var keyStorePass = jwtKeyStoreProperties.getKeyStorePass();
		var keyPairAlias = jwtKeyStoreProperties.getKeyPairAlias();
		
		var keyStoreKeyFactory = new KeyStoreKeyFactory(jksResource, keyStorePass.toCharArray());
		var keyPair = keyStoreKeyFactory.getKeyPair(keyPairAlias);
		
		jwtAccessTokenConverter.setKeyPair(keyPair); // Chave assimetrica
		
		return jwtAccessTokenConverter;
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
