package com.algaworks.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@JsonRootName("gastronomia") -> Muda o nome do objeto na representacao;
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_cozinha")
public class Cozinha {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@JsonIgnore -> Ignora a propriedade na hora de serializar a representacao (Remove da representacao);
//	@JsonProperty("titulo") -> Forma que a propriedade será representada no JSON;
	@Column(nullable = false)
	private String nome;

}
