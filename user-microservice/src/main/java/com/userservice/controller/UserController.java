package com.userservice.controller;

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

import com.userservice.dto.DepartmentDto;
import com.userservice.dto.OrderRequestDto;
import com.userservice.dto.ResponseDto;
import com.userservice.dto.UserDto;
import com.userservice.model.User;
import com.userservice.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

//	@GetMapping("get-all-users")
//	public ResponseEntity<List<User>> getAllDepartment() {
//		List<User> userResponses = userService.getAllUsers();
//		return new ResponseEntity<List<User>>(userResponses, HttpStatus.CREATED);
//	}
	@GetMapping("get-all-users")
	public ResponseEntity<List<UserDto>> getAllUsersWithDepartment() {
		List<UserDto> userResponses= userService.getAllUsersWithDepartments();
		return new ResponseEntity<List<UserDto>>(userResponses, HttpStatus.CREATED);
	}

	@PostMapping("/add-user")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		User savedUser = userService.addUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	@PostMapping("/update-user/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") long userId, @RequestBody User user) {
		User UpdatedUser = userService.updateUser(userId, user);
		return new ResponseEntity<User>(UpdatedUser, HttpStatus.CREATED);
	}

//	@GetMapping("get-user-by-id/{id}")
//	public ResponseEntity<ResponseDto> getUser(@PathVariable("id") Long userId) {
//		ResponseDto responseDto = userService.getUser(userId);
//		return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
//	}
	@GetMapping("get-user-by-id/{id}")
	public ResponseEntity<UserDto> getUserDetail(@PathVariable("id") Long userId) {
		UserDto userDto = userService.getUserDetail(userId);
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}

	@DeleteMapping("delete--user/{id}")
	public ResponseEntity<String> deleteDepartment(@PathVariable("id") long userId) {
		userService.deleteUser(userId);
		return new ResponseEntity<String>("User delete successfully", HttpStatus.CREATED);

	}

	@GetMapping("/department-names/{departmentName}")
	public ResponseEntity<DepartmentDto> getDepartmentByDepartName(@PathVariable("departmentName") String departmentName) {
		DepartmentDto department = userService.getDepartmentByDepartName(departmentName);
		return new ResponseEntity<DepartmentDto>(department, HttpStatus.OK);
	}
	
	@GetMapping("/department-name/{departmentName}")
	public ResponseEntity<List<UserDto>> getDepartmentByDepartmentName(@PathVariable("departmentName") String departmentName) {
		List<UserDto> listDepartmentName = userService.getDepartmentByDepartmentName(departmentName);
		return new ResponseEntity<List<UserDto>>(listDepartmentName, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<ResponseDto> placeOrders(@RequestBody OrderRequestDto orderRequest) {
		ResponseDto savedOrder= userService.placeOrders(orderRequest);
		return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
	}
	
	@GetMapping("/get-order-userId/{id}")
	public ResponseEntity<ResponseDto> getOrderByUserId(@PathVariable("id") long userId) {
		ResponseDto getAllUserOrderList = userService.getOrderByUserId(userId);
		return new ResponseEntity<ResponseDto>(getAllUserOrderList, HttpStatus.OK);
	}
	
	@PostMapping("/add-order")
	public ResponseEntity<OrderRequestDto> placeOrder(@RequestBody OrderRequestDto orderRequest) {
		OrderRequestDto savedOrder= userService.placeOrder(orderRequest);
		return new ResponseEntity<OrderRequestDto>(savedOrder, HttpStatus.CREATED);
	}
	
	@GetMapping("order-by-id/{id}")
	public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable("id") Long orderId) {
		OrderRequestDto responseDto = userService.getOrderById(orderId);
		return new ResponseEntity<OrderRequestDto>(responseDto, HttpStatus.OK);
	}
	
}
