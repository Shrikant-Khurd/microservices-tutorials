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

import com.userservice.dto.AddressDto;
import com.userservice.dto.ApiResponse;
import com.userservice.dto.OrderRequestDto;
import com.userservice.dto.ResponseDto;
import com.userservice.dto.UserDto;
import com.userservice.model.Address;
import com.userservice.model.User;
import com.userservice.model.UserRole;
import com.userservice.repository.UserRoleRepository;
import com.userservice.service.UserService;
import com.userservice.util.SequenceGenaratorService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleRepository demo1;
	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;

	
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
	@PostMapping("/add-user")
	public ResponseEntity<ApiResponse> addUser(@RequestBody UserDto user) {
		ApiResponse savedUser = userService.addUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	@PostMapping("/update-user/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") long userId, @RequestBody User user) {
		User updatedUser = userService.updateUser(userId, user);
		return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
	}

	@GetMapping("getlist")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> allUsers = userService.getAllUsers();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@GetMapping("get-user-by-idd/{id}")
	public ResponseEntity<ResponseDto> getUser(@PathVariable("id") Long userId) {
		ResponseDto responseDto = userService.getUser(userId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@GetMapping("get-user-by-id/{id}")
	public ResponseEntity<UserDto> getUserDetail(@PathVariable("id") Long userId) {
		UserDto userDto = userService.getUserDetail(userId);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@DeleteMapping("delete--user/{id}")
	public ResponseEntity<String> deleteDepartment(@PathVariable("id") long userId) {
		userService.deleteUser(userId);
		return new ResponseEntity<>("User delete successfully", HttpStatus.CREATED);
	}

	// Address api's
	@PostMapping("add-address")
	public ResponseEntity<AddressDto> addAddress(@RequestBody AddressDto address) {
		AddressDto addAddress = userService.addAddress(address);
		return new ResponseEntity<>(addAddress, HttpStatus.OK);
	}

	@GetMapping("get-address")
	public ResponseEntity<List<Address>> getAddresses() {
		List<Address> demos = userService.getAllAddress();
		return new ResponseEntity<>(demos, HttpStatus.OK);
	}

	@PostMapping("add-address/{id}")
	public ResponseEntity<UserDto> addUserAddress(@RequestBody AddressDto address, @PathVariable long id) {
		UserDto addAddress = userService.addUserAddress(address, id);
		return new ResponseEntity<>(addAddress, HttpStatus.OK);
	}

	@GetMapping("/address-detail-byId/{addressId}")
	public ResponseEntity<AddressDto> getAddressDetailByAddressId(@PathVariable("addressId") long addressId) {
		AddressDto getAddressDetails = userService.getAddressDetailByAddressId(addressId);
		return new ResponseEntity<>(getAddressDetails, HttpStatus.OK);
	}

	@GetMapping("get-address-byUserId/{id}")
	public ResponseEntity<List<AddressDto>> getAddressesByUserId(@PathVariable long id) {
		List<AddressDto> demos = userService.getAddressesByUserId(id);
		return new ResponseEntity<>(demos, HttpStatus.OK);
	}

	// Order api's
	@PostMapping("/add-order")
	public ResponseEntity<ResponseDto> placeOrders(@RequestBody OrderRequestDto orderRequest) {
		ResponseDto savedOrder = userService.placeOrders(orderRequest);
		return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
	}

	@GetMapping("/get-order-userId/{id}")
	public ResponseEntity<ResponseDto> getOrderByUserId(@PathVariable("id") long userId) {
		ResponseDto getAllUserOrderList = userService.getOrderByUserId(userId);
		return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
	}

	@GetMapping("/get-orderDetail-userId/{id}")
	public ResponseEntity<List<OrderRequestDto>> getAllOrderByUserId(@PathVariable("id") long userId) {
		List<OrderRequestDto> getAllUserOrderList = userService.getOrderDetailByUserId(userId);
		return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
	}

	@GetMapping("order-by-orderId/{id}")
	public ResponseEntity<OrderRequestDto> getOrderByOrderId(@PathVariable("id") Long orderId) {
		OrderRequestDto responseDto = userService.getOrderByOrderId(orderId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	// Role api's
	@PostMapping("add-role")
	public ResponseEntity<UserRole> addrole(@RequestBody UserRole userDemo) {
		userDemo.setId(sequenceGenaratorService.generateSequence(UserRole.SEQUENCE_NAME));
		UserRole d = demo1.save(userDemo);
		return new ResponseEntity<>(d, HttpStatus.OK);
	}

	@GetMapping("/roles/{roleName}")
	public ResponseEntity<List<UserDto>> getUsersByRoleName(@PathVariable("roleName") String roleName) {
		List<UserDto> users = userService.getUsersByRoleName(roleName);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
}
