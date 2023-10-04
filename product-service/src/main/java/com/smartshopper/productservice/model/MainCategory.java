package com.smartshopper.productservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
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
