package com.authservice.controller;

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

import com.authservice.dto.AddressDto;
import com.authservice.dto.ApiResponse;
import com.authservice.dto.LoginRequest;
import com.authservice.dto.LoginResponse;
import com.authservice.dto.UserDto;
import com.authservice.dto.UserRoleDto;
import com.authservice.model.UserRole;
import com.authservice.service.AuthJwtService;
import com.authservice.service.UserService;

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
		System.out.println("method called.........");
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
		List<UserRoleDto> role = userService.getAllRoles();
		return new ResponseEntity<>(role, HttpStatus.OK);
	}
	
	@PostMapping("/add-role")
	public ResponseEntity<UserRole> addRole(@RequestBody UserRole role) {
		UserRole userRole = userService.addUserRole(role);
		return new ResponseEntity<>(userRole, HttpStatus.OK);
	}
	
	@GetMapping("/role/{roleId}")
	public ResponseEntity<UserRole> getUsersByRoleId(@PathVariable("roleId") long roleId) {
		UserRole role = userService.getUsersByRoleId(roleId);
		return new ResponseEntity<>(role, HttpStatus.OK);
	}


	@GetMapping("/roles/{roleName}")
	public ResponseEntity<List<UserDto>> getUsersByRoleName(@PathVariable("roleName") String roleName) {
		List<UserDto> users = userService.getUsersByRoleName(roleName);
		return new ResponseEntity<>(users, HttpStatus.OK);
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

	@PostMapping("get-user-by-id")
	public ResponseEntity<UserDto> getUserDetail() {
		System.out.println("method called....");
		UserDto userDto = userService.getUserDetail();
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
	@PostMapping("add-address")
	public ResponseEntity<ApiResponse> addUserAddress(@RequestBody AddressDto address) {
		ApiResponse addAddress = userService.addUserAddress(address);
		return new ResponseEntity<>(addAddress, addAddress.getHttpStatus());
	}
}
