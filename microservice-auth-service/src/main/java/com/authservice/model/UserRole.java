package com.authservice.model;

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
@Document(collection="user-role")
public class UserRole {

	@Transient
	public static final String SEQUENCE_NAME="userRole_sequence";
	
	@Id
	private Long roleId;
	private String roleName;
}
