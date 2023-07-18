package com.mongodb.productservice.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.productservice.dto.ApiResponse;
import com.mongodb.productservice.dto.LoginRequest;
import com.mongodb.productservice.dto.LoginResponse;
import com.mongodb.productservice.dto.UserDto;
import com.mongodb.productservice.model.User;
import com.mongodb.productservice.model.UserRole;
import com.mongodb.productservice.security.JwtFilter;
import com.mongodb.productservice.service.AuthJwtService;
import com.mongodb.productservice.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthJwtService authJwtService;
	@Autowired
	private UserService userService;

	@PostMapping({ "/authenticate" })
	public ResponseEntity<LoginResponse> createJwtToken(@RequestBody LoginRequest jwtRequest) {
		LoginResponse resp = authJwtService.createJwtToken(jwtRequest);
		return new ResponseEntity<>(resp, HttpStatus.OK);

	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse> addUser(@RequestBody UserDto user) {
		ApiResponse savedUser = userService.addUser(user);
		return new ResponseEntity<>(savedUser, savedUser.getHttpStatus());
	}

	/*@PostMapping("/current-user")
	public ResponseEntity<User> currectUser() {
		User userDetails = authJwtService.loadUserByEmail(JwtFilter.CURRENT_USER);
		return new ResponseEntity<>(userDetails, HttpStatus.OK);
	}*/

	@PostMapping("/current-userRole")
	public ResponseEntity<UserRole> currectUserRole() {
//		Set<UserRole> userDetails = JwtFilter.CURRENT_USER_ROLE;
		UserRole role = authJwtService.currectUserRole();
		return new ResponseEntity<>(role, HttpStatus.OK);
	}

}
