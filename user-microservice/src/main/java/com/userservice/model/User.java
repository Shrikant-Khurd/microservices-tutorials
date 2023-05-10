package com.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection="users")
public class User {
	
	@Transient
	public static final String SEQUENCE_NAME="user_sequence";
	
	@Id
	private long id;
	private String firstName;
	private String lastName;
	private String email;
	private long departmentId;

}
