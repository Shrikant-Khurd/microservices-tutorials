package com.mongodb.productservice.dto;

import java.util.Map;
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

	

	/*public LoginResponse(long id, Set<UserRole> userRole, Map<String, String> newGeneratedToken) {
		super();
		this.id = id;
		this.userRole = userRole;
		this.newGeneratedToken = newGeneratedToken;
	}
	private String accessToken;
	private long id;
	private Set<UserRole> userRole;*/
	//private Map<String, String> newGeneratedToken;
	
	private String accessToken;
}
