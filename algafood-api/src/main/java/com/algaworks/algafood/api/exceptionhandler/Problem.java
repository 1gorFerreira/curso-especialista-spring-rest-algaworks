package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)//Só inclua na representação valores que não sejam NULL;
@Getter
@Builder
public class Problem {
	
	private Integer status;
	private String type;
	private String title;
	private LocalDateTime timestamp;
	private String detail;
	
	private String userMessage;
	private List<Field> fields;
	
	@Getter
	@Builder
	public static class Field {
		
		private String name;
		private String userMessage;
		
	}
}
