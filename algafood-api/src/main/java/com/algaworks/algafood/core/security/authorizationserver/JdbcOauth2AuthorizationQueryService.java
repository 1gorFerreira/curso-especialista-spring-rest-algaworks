package com.algaworks.algafood.core.security.authorizationserver;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.List;

public class JdbcOauth2AuthorizationQueryService implements OAuth2AuthorizationQueryService{

    private final JdbcOperations jdbcOperations;
    private final RowMapper<RegisteredClient> registeredClientRowMapper;
    private final String LIST_AUTHORIZED_CLIENTS = "SELECT rc.* FROM oauth2_authorization_consent c " +
            "INNER JOIN oauth2_registered_client rc on rc.id = c.registered_client_id " +
            "WHERE c.principal_name = ? ";

    public JdbcOauth2AuthorizationQueryService(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        this.registeredClientRowMapper = new JdbcRegisteredClientRepository.RegisteredClientRowMapper();
    }

    @Override
    public List<RegisteredClient> listClientsWithConsent(String principalName) {
        return this.jdbcOperations.query(LIST_AUTHORIZED_CLIENTS, registeredClientRowMapper, principalName);
    }

}
