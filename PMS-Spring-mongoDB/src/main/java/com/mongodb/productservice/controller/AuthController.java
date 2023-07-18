package com.mongodb.productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.productservice.dto.ApiResponse;
import com.mongodb.productservice.dto.LoginRequest;
import com.mongodb.productservice.dto.LoginResponse;
import com.mongodb.productservice.dto.UserDto;
import com.mongodb.productservice.service.AuthJwtService;
import com.mongodb.productservice.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthJwtService authJwtService;
	@Autowired
	private UserService userService;

	@PostMapping({ "/authenticate" })
	public ResponseEntity<LoginResponse> createJwtToken(@RequestBody LoginRequest jwtRequest) {

		return new ResponseEntity<>(authJwtService.createJwtToken(jwtRequest), HttpStatus.OK);

	}

	@PostMapping("/add-user")
	public ResponseEntity<ApiResponse> addUser(@RequestBody UserDto user) {
		ApiResponse savedUser = userService.addUser(user);
		return new ResponseEntity<>(savedUser, savedUser.getHttpStatus());
	}
	
	@GetMapping("/restricted")
    public ResponseEntity<?> getRestrictedMessage() {
        return new ResponseEntity<>("This is a restricted message", HttpStatus.OK);
    }
}
