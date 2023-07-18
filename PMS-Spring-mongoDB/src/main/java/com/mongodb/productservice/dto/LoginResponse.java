package com.mongodb.productservice.dto;

import java.util.Set;

import com.mongodb.productservice.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponse {

	private String accessToken;
	private long id;
	private Set<UserRole> userRole;

}
