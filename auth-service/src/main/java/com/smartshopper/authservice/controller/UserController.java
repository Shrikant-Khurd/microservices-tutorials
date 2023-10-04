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
import org.springframework.web.bind.annotation.RestController;

import com.smartshopper.authservice.dto.AddressDto;
import com.smartshopper.authservice.dto.ApiResponse;
import com.smartshopper.authservice.dto.UserDto;
import com.smartshopper.authservice.model.Address;
import com.smartshopper.authservice.service.UserService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/getlist")
	public ResponseEntity<ApiResponse> getAllUsers() {
		ApiResponse allUsers = userService.getAllUsers();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@PostMapping("/get-user-by-id")
	public ResponseEntity<UserDto> getUserDetail() {
		UserDto userDto = userService.getUserDetail();
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@PostMapping("/user-details")
	public ResponseEntity<UserDto> getUserDetails() {
		UserDto userDto = userService.getUserDetail();
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
	@PostMapping("/userData/{id}")
	public ResponseEntity<UserDto> getUserDetailByUserId(@PathVariable("id") long userId) {
		UserDto userDto = userService.getUserDetail(userId);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
	// Address api's
	@GetMapping("/addresses")
	public ResponseEntity<List<Address>> getAddresses() {
		List<Address> addresses = userService.getAllAddresses();
		return new ResponseEntity<>(addresses, HttpStatus.OK);
	}

	@PostMapping("/add-address")
	public ResponseEntity<ApiResponse> addUserAddress(@RequestBody AddressDto addressDto) {
		ApiResponse addAddressResponse = userService.addUserAddress(addressDto);
		return new ResponseEntity<>(addAddressResponse, addAddressResponse.getHttpStatus());
	}

	@GetMapping("/addresses/{addressId}")
	public ResponseEntity<ApiResponse> getAddressDetailByAddressId(@PathVariable("addressId") long addressId) {
		ApiResponse getAddressDetailsResponse = userService.getAddressDetailByAddressId(addressId);
		return new ResponseEntity<>(getAddressDetailsResponse, HttpStatus.OK);
	}

	@GetMapping("/address-id/{addressId}")
	public ResponseEntity<AddressDto> getAddressByAddressId(@PathVariable("addressId") long addressId) {
		AddressDto getAddressDetails = userService.getAddressByAddressId(addressId);
		return new ResponseEntity<>(getAddressDetails, HttpStatus.OK);
	}

	@GetMapping("/user-addresses")
	public ResponseEntity<ApiResponse> getAddressesByUserId() {
		ApiResponse userAddressesResponse = userService.getAddressesByUserId();
		return new ResponseEntity<>(userAddressesResponse, HttpStatus.OK);
	}

	@PostMapping("/addresses/{addressId}")
	public ResponseEntity<ApiResponse> updateUserAddress(@RequestBody AddressDto updatedAddressDto,
			@PathVariable("addressId") long addressId) {
		ApiResponse updateAddressResponse = userService.updateUserAddress(updatedAddressDto, addressId);
		return new ResponseEntity<>(updateAddressResponse, updateAddressResponse.getHttpStatus());
	}

	@PostMapping("/addresses/{addressId}/set-default")
	public ResponseEntity<ApiResponse> setAsDefaultUserAddress(@PathVariable("addressId") long addressId) {
		ApiResponse setDefaultAddressResponse = userService.setAsDefaultUserAddress(addressId);
		return new ResponseEntity<>(setDefaultAddressResponse, setDefaultAddressResponse.getHttpStatus());
	}

	@PostMapping("/addresses/{addressId}/delete")
	public ResponseEntity<ApiResponse> deleteAddress(@PathVariable("addressId") long addressId) {
		ApiResponse deleteAddressResponse = userService.deleteAddress(addressId);
		return new ResponseEntity<>(deleteAddressResponse, deleteAddressResponse.getHttpStatus());
	}
}
