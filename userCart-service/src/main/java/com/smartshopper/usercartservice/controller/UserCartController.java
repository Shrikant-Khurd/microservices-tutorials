package com.smartshopper.usercartservice.controller;

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

import com.smartshopper.usercartservice.dto.ApiResponse;
import com.smartshopper.usercartservice.dto.UserCartRequestDto;
import com.smartshopper.usercartservice.service.UserCartService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class UserCartController {

	private final UserCartService userCartService;

	@PostMapping("/add-to-cart")
	public ResponseEntity<ApiResponse> addToCart(@RequestBody UserCartRequestDto orderRequest) {
		ApiResponse savedCart = userCartService.addToCart(orderRequest);
		return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
	}

	@GetMapping("cart-by-cartId/{cartId}")
	public ResponseEntity<UserCartRequestDto> getCart(@PathVariable("cartId") Long cartId) {
		UserCartRequestDto responseDto = userCartService.getCart(cartId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@GetMapping("/getCartDetails")
	public ResponseEntity<ApiResponse> getCartByUserId() {
		ApiResponse getAllUserOrderList = userCartService.getCartByUserId();
		return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
	}

	@PostMapping("/deleteCartItem/{cartId}")
	public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartId") Long cartId) {
		ApiResponse savedCart = userCartService.deleteCartItem(cartId);
		return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
	}

	@GetMapping({ "/getProductDetails/{productId}" })
	public ResponseEntity<ApiResponse> getProductDetails(@PathVariable(name = "productId") Integer productId) {
		ApiResponse getProductDetails=  userCartService.getProductDetails(productId);
		return new ResponseEntity<>(getProductDetails, HttpStatus.OK);
	}
	@PostMapping({ "/removeProductFromOrderAndCart/{productId}" })
	public ResponseEntity<ApiResponse>  removeCartByProductId(@PathVariable(name = "productId") Long productId) {
		ApiResponse getProductDetails=  userCartService. removeCartByProductId(productId);
		return new ResponseEntity<>(getProductDetails, HttpStatus.OK);
	}
	@PostMapping({ "/udateCartProductQuantityAndCartStatus" })
	public ResponseEntity<ApiResponse>  udateCartProductQuantityAndCartStatus(@RequestParam("cartProductId") long cartProductId,
			@RequestParam("quantity") long quantity, @RequestParam("isCartSelected") boolean isCartSelected) {
		ApiResponse getProductDetails=  userCartService. udateCartProductQuantityAndCartStatus(cartProductId,quantity,isCartSelected);
		return new ResponseEntity<>(getProductDetails, HttpStatus.OK);
	}
}
