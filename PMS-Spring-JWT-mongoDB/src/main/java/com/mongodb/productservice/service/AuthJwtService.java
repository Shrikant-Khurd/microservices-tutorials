package com.mongodb.productservice.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mongodb.productservice.dto.LoginRequest;
import com.mongodb.productservice.dto.LoginResponse;
import com.mongodb.productservice.exception.ResourceNotFoundException;
import com.mongodb.productservice.model.User;
import com.mongodb.productservice.model.UserRole;
import com.mongodb.productservice.repository.UserRepository;
import com.mongodb.productservice.security.JwtFilter;
import com.mongodb.productservice.security.JwtTokenProvider;

@Service
public class AuthJwtService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtTokenProvider jwtUtil;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public LoginResponse createJwtToken(LoginRequest jwtRequest) {

		// System.out.println(jwtRequest.getEmail());
		// System.out.println(jwtRequest.getPassword());
		User user = loadUserByEmail(jwtRequest.getEmail());

		if (user != null) {
			boolean isPasswordMatch = passwordEncoder.matches(jwtRequest.getPassword(), user.getPassword());
			if (isPasswordMatch) {

//			Map<String, String> newGeneratedToken = jwtUtil.generateJwtToken(user);
				String newGeneratedToken = jwtUtil.generateJwtToken(user);
				return new LoginResponse(newGeneratedToken);
			} else {
				throw new ResourceNotFoundException("Incorrect password please enter correct password.");
			}
		} else {
			throw new ResourceNotFoundException("Email not found please enter valid email.");

		}
	}

	public User loadUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public UserRole currectUserRole() {
		Set<UserRole> userDetails = JwtFilter.CURRENT_USER_ROLE;
		UserRole userRole = new UserRole();
		for (UserRole role : userDetails) {
			userRole.setId(role.getId());
			userRole.setRoleName(role.getRoleName());
		}
		return userRole;
	}

}
