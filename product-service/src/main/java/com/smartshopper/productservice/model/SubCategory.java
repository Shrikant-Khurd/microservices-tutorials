package com.smartshopper.productservice.model;

import java.util.List;
import java.util.Set;

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
@Document("sub_category")
public class SubCategory {

	@Transient
	public static final String SEQUENCE_NAME = "subCategory_sequence";

	@Id
	private long categoryId;
	private String categoryName;
	@DBRef
	@JsonIgnore
	private MainCategory mainCategory;
//	private Set<MainCategory> mainCategory;

}
