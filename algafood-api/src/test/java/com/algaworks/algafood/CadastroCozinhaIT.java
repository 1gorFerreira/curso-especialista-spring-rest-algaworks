package com.algaworks.algafood;

import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

//webEnvironment vai levantar nossa aplicação para fazermos requisições reais;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CadastroCozinhaIT {

	//Gera uma porta aleatória para rodar a aplicação;
	@LocalServerPort
	private int port;
	
	@Autowired
	private Flyway flyway;
	
	//Será executado antes de cada teste:
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();//Esse método ajudará a debbugar caso o teste falhe;
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		//Chmamos o migrate para executar o afterMigrate para "resetar" o banco de dados;
		flyway.migrate();
	}
	
//	@Test
//	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
//		Esse método ajudará a debbugar caso o teste falhe;
//		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
//		RestAssured.given()
//			.basePath("/cozinhas")
//			.port(port)
//			.accept(ContentType.JSON)
//		.when()
//			.get()
//		.then()
//			.statusCode(HttpStatus.OK.value());
//	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveConter4Cozinhas_QuandoConsultarCozinhas() {
		
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(4))
			.body("nome", Matchers.hasItems("Indiana", "Tailandesa"));
	}

	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinhas() {
		
		RestAssured.given()
			.body("{ \"nome\": \"Chinesa\" }")
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
}
