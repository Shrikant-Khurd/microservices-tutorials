package com.authservice.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.authservice.dto.LoginRequest;
import com.authservice.dto.LoginResponse;
import com.authservice.exception.ResourceNotFoundException;
import com.authservice.model.User;
import com.authservice.model.UserRole;
import com.authservice.repository.UserRepository;
import com.authservice.security.JwtFilter;
import com.authservice.security.JwtTokenProvider;

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
				System.out.println("wrong password");
				throw new ResourceNotFoundException("Incorrect password please enter correct password.");
			}
		} else {
			System.out.println("wrong email");
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
			userRole.setRoleId(role.getRoleId());
			userRole.setRoleName(role.getRoleName());
		}
		return userRole;
	}

	public void validateToken(String token) {
		jwtUtil.validateJwtToken(token);
		
	}

}
