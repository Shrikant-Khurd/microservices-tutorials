package com.microservice.productservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Document("main_category")
public class MainCategory {

	@Transient
	public static final String SEQUENCE_NAME = "mainCategory_sequence";

	@Id
	private long categoryId;
	private String categoryName;

}
