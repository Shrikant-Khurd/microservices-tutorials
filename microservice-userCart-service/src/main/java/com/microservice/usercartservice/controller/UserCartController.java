package com.microservice.usercartservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.usercartservice.dto.UserCartRequestDto;
import com.microservice.usercartservice.dto.ResponseDto;
import com.microservice.usercartservice.service.UserCartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class UserCartController {

	private final UserCartService orderService;

	@PostMapping("/add-to-cart")
	public ResponseEntity<ResponseDto> addToCart(@RequestBody UserCartRequestDto orderRequest) {
		ResponseDto savedCart = orderService.addToCart(orderRequest);
		return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
	}

	@GetMapping("cart-by-cartId/{cartId}")
	public ResponseEntity<UserCartRequestDto> getCart(@PathVariable("cartId") Long cartId) {
		UserCartRequestDto responseDto = orderService.getCart(cartId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@GetMapping("/all-carts")
	public ResponseEntity<List<UserCartRequestDto>> getAllOrders() {
		List<UserCartRequestDto> allOrderList = orderService.getAllOrders();
		return new ResponseEntity<>(allOrderList, HttpStatus.OK);
	}

	@GetMapping("/get-cartDetail-userId/{userId}")
	public ResponseEntity<List<UserCartRequestDto>> getAllCartByUserId(@PathVariable("userId") long userId) {
		List<UserCartRequestDto> getAllUserCartList = orderService.getAllCartByUserId(userId);
		return new ResponseEntity<>(getAllUserCartList, HttpStatus.OK);
	}

	@GetMapping("/get-cart-userId/{userId}")
	public ResponseEntity<ResponseDto> getCartByUserId(@PathVariable("userId") long userId) {
		ResponseDto getAllUserOrderList = orderService.getCartByUserId(userId);
		return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
	}
}
