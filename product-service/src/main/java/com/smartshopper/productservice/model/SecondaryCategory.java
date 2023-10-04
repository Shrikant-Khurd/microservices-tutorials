package com.smartshopper.productservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Document("secondary_category")
public class SecondaryCategory {

	@Transient
	public static final String SEQUENCE_NAME = "secondaryCategory_sequence";

	@Id
	private long categoryId;
	private String categoryName;
	@DBRef
	@JsonIgnore
	private SubCategory subCategory;

}
