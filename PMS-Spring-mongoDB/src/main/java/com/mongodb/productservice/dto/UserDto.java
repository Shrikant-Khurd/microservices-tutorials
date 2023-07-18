package com.mongodb.productservice.dto;

import java.time.Instant;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mongodb.productservice.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserDto {
	private long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private long contactNumber;
	private Instant createdAt;
	private Instant updatedAt;
	private Boolean accountStatus;
	private Set<UserRole> roles;

	
}