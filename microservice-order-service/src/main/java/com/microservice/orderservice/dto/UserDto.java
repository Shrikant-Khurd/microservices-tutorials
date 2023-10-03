package com.microservice.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private long id;
	private String firstName;
	private String lastName;
	private String email;
	private long contactNumber;
	//private Set<UserRoleDto> roles;
}