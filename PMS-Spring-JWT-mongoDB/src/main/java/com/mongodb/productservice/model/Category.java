package com.mongodb.productservice.model;

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
@Document("category")
public class Category {

	@Transient
	public static final String SEQUENCE_NAME = "category_sequence";

	@Id
	private long categoryId;

	private String categoryName;

}
