package com.mongodb.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.productservice.dto.ApiResponse;
import com.mongodb.productservice.dto.UserDto;
import com.mongodb.productservice.model.UserRole;
import com.mongodb.productservice.repository.UserRoleRepository;
import com.mongodb.productservice.service.UserService;
import com.mongodb.productservice.utils.SequenceGenaratorService;

@RestController
@RequestMapping("/api/user")
//@PreAuthorize("hasRole('ADMIN')")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/data/{id}")
	public ResponseEntity<ApiResponse> getById(@PathVariable long id) {
		ApiResponse userRespose = userService.getById(id);
		return new ResponseEntity<>(userRespose, userRespose.getHttpStatus());
	}

//	@PostMapping("/add-user")
//	public ResponseEntity<UserDto> addUser(@RequestBody UserDto user) {
//		UserDto savedUser = userService.addUser(user);
//		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
//	}
//	@PostMapping("/add-user")
//	public ResponseEntity<ApiResponse> addUser(@RequestBody UserDto user) {
//		ApiResponse savedUser = userService.addUser(user);
//		return new ResponseEntity<>(savedUser, savedUser.getHttpStatus());
//	}

	@GetMapping("getlist")
	public ResponseEntity<ApiResponse> getAllUsers() {
		ApiResponse allUsers = userService.getAllUsers();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@GetMapping("get-user-by-id/{id}")
	public ResponseEntity<UserDto> getUserDetail(@PathVariable("id") Long userId) {
		UserDto userDto = userService.getUserDetail(userId);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	// Role api's
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
}
