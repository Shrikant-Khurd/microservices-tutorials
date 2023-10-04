package com.smartshopper.authservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartshopper.authservice.dto.ApiResponse;
import com.smartshopper.authservice.dto.LoginRequest;
import com.smartshopper.authservice.dto.LoginResponse;
import com.smartshopper.authservice.dto.UserDto;
import com.smartshopper.authservice.dto.UserRoleDto;
import com.smartshopper.authservice.model.UserRole;
import com.smartshopper.authservice.service.AuthJwtService;
import com.smartshopper.authservice.service.UserService;

@CrossOrigin("http://localhost:4200")
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

	@PostMapping("/validate")
	public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
		authJwtService.validateToken(token);
		return new ResponseEntity<>("Token is valid", HttpStatus.OK);
	}

	// Roles APIs
	@GetMapping("/roles")
	public ResponseEntity<List<UserRoleDto>> getAllRoles() {
		List<UserRoleDto> role = authJwtService.getAllRoles();
		return new ResponseEntity<>(role, HttpStatus.OK);
	}

	@PostMapping("/add-role")
	public ResponseEntity<UserRoleDto> addRole(@RequestBody UserRoleDto role) {
		UserRoleDto userRole = authJwtService.addUserRole(role);
		return new ResponseEntity<>(userRole, HttpStatus.OK);
	}

	@GetMapping("/role/{roleId}")
	public ResponseEntity<UserRole> getUsersByRoleId(@PathVariable("roleId") long roleId) {
		UserRole role = authJwtService.getUsersByRoleId(roleId);
		return new ResponseEntity<>(role, HttpStatus.OK);
	}

	@GetMapping("/roles/{roleName}")
	public ResponseEntity<List<UserDto>> getUsersByRoleName(@PathVariable("roleName") String roleName) {
		List<UserDto> users = authJwtService.getUsersByRoleName(roleName);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping("/current-userRole")
	public ResponseEntity<UserRole> currectUserRole() {
		UserRole role = authJwtService.currectUserRole();
		return new ResponseEntity<>(role, HttpStatus.OK);
	}

}
