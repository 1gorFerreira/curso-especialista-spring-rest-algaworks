package com.algaworks.algafood.auth.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Validated
@Component
@ConfigurationProperties("algafood.jwt.keystore")
public class JwtKeyStoreProperties {

	@NotBlank
	private String path;
	
	@NotBlank
	private String keyStorePass;
	
	@NotBlank
	private String keyPairAlias;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getKeyStorePass() {
		return keyStorePass;
	}

	public void setKeyStorePass(String keyStorePass) {
		this.keyStorePass = keyStorePass;
	}

	public String getKeyPairAlias() {
		return keyPairAlias;
	}

	public void setKeyPairAlias(String keyPairAlias) {
		this.keyPairAlias = keyPairAlias;
	}
}
