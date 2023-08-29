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
import org.springframework.web.bind.annotation.RestController;

import com.authservice.dto.AddressDto;
import com.authservice.dto.ApiResponse;
import com.authservice.dto.OrderRequestDto;
import com.authservice.dto.ResponseDto;
import com.authservice.dto.UserDto;
import com.authservice.model.Address;
import com.authservice.service.UserService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;



	@GetMapping("getlist")
	public ResponseEntity<ApiResponse> getAllUsers() {
		ApiResponse allUsers = userService.getAllUsers();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	/*
	 * @GetMapping("get-user-by-id/{id}") public ResponseEntity<UserDto>
	 * getUserDetail(@PathVariable("id") Long userId) { UserDto userDto =
	 * userService.getUserDetail(userId); return new ResponseEntity<>(userDto,
	 * HttpStatus.OK); }
	 */
	@PostMapping("get-user-by-id")
	public ResponseEntity<UserDto> getUserDetail() {
		UserDto userDto = userService.getUserDetail();
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@PostMapping("user-details")
	public ResponseEntity<UserDto> getUserDetails() {
		System.out.println("method called....");
		UserDto userDto = userService.getUserDetail();
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	// Address api's
//	@PostMapping("add-address")
//	public ResponseEntity<AddressDto> addAddress(@RequestBody AddressDto address) {
//		AddressDto addAddress = userService.addAddress(address);
//		return new ResponseEntity<>(addAddress, HttpStatus.OK);
//	}

	@GetMapping("get-address")
	public ResponseEntity<List<Address>> getAddresses() {
		List<Address> demos = userService.getAllAddress();
		return new ResponseEntity<>(demos, HttpStatus.OK);
	}

	@PostMapping("add-address")
	public ResponseEntity<ApiResponse> addUserAddress(@RequestBody AddressDto address) {
		ApiResponse addAddress = userService.addUserAddress(address);
		return new ResponseEntity<>(addAddress, addAddress.getHttpStatus());
	}

	@GetMapping("address-detail/{addressId}")
	public ResponseEntity<ApiResponse> getAddressDetailByAddressId(@PathVariable("addressId") long addressId) {
		ApiResponse getAddressDetails = userService.getAddressDetailByAddressId(addressId);
		return new ResponseEntity<>(getAddressDetails, HttpStatus.OK);
	}
	@GetMapping("address-id/{addressId}")
	public ResponseEntity<AddressDto> getAddressByAddressId(@PathVariable("addressId") long addressId) {
		AddressDto getAddressDetails = userService.getAddressByAddressId(addressId);
		return new ResponseEntity<>(getAddressDetails, HttpStatus.OK);
	}

	@GetMapping("get-all-user-address")
	public ResponseEntity<ApiResponse> getAddressesByUserId() {
		ApiResponse userAddresses = userService.getAddressesByUserId();
		return new ResponseEntity<>(userAddresses, HttpStatus.OK);
	}

	@PostMapping("update-address/{addressId}")
	public ResponseEntity<ApiResponse> updateUserAddress(@RequestBody AddressDto updatedAddressDto,
			@PathVariable("addressId") long addressId) {
		ApiResponse addAddress = userService.updateUserAddress(updatedAddressDto, addressId);
		return new ResponseEntity<>(addAddress, addAddress.getHttpStatus());
	}

	@PostMapping("set-as-default-address/{addressId}")
	public ResponseEntity<ApiResponse> setAsDefaultUserAddress(@PathVariable("addressId") long addressId) {
		ApiResponse addAddress = userService.setAsDefaultUserAddress(addressId);
		return new ResponseEntity<>(addAddress, addAddress.getHttpStatus());
	}

	@PostMapping("delete-address/{addressId}")
	public ResponseEntity<ApiResponse> deleteAddress(@PathVariable("addressId") long addressId) {
		ApiResponse deleteAddress = userService.deleteAddress(addressId);
		return new ResponseEntity<>(deleteAddress, deleteAddress.getHttpStatus());
	}

	// Order api's
		@PostMapping("add-order")
		public ResponseEntity<ResponseDto> placeOrders(@RequestBody OrderRequestDto orderRequest) {
			ResponseDto savedOrder = userService.placeOrders(orderRequest);
			return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
		}

		@GetMapping("get-order-userId")
		public ResponseEntity<ApiResponse> getOrderByUserId() {
			ApiResponse getAllUserOrderList = userService.getOrderByUserId();
			return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
		}

		@GetMapping("get-orderDetail-userId/{id}")
		public ResponseEntity<ApiResponse> getAllOrderByUserId(@PathVariable("id") long userId) {
			ApiResponse getAllUserOrderList = userService.getOrderDetailByUserId(userId);
			return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
		}
	
}
