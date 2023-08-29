package com.microservice.usercartservice.mapper;

import com.microservice.usercartservice.dto.UserCartRequestDto;
import com.microservice.usercartservice.model.UserCart;

public class CartMapper {

	public static UserCartRequestDto mapToUserCart(UserCart cartRequest) {
		UserCartRequestDto cartRequestDto = new UserCartRequestDto();
		cartRequestDto.setCartId(cartRequest.getId());
		cartRequestDto.setUserId(cartRequest.getUserId());
		cartRequestDto.setCartDate(cartRequest.getCartDate());
		cartRequestDto.setCartNumber(cartRequest.getCartNumber());
		cartRequestDto.setQuantity(cartRequest.getQuantity());
		cartRequestDto.setCartSelected(cartRequest.isCartSelected());
		

		return cartRequestDto;
	}
	public static UserCart mapUserCartRequestDtoToUserCart(UserCartRequestDto cartRequest) {
		UserCart cartRequestDto = new UserCart();
		cartRequestDto.setId(cartRequest.getCartId());
		cartRequestDto.setUserId(cartRequest.getUserId());
		cartRequestDto.setCartDate(cartRequest.getCartDate());
		cartRequestDto.setCartNumber(cartRequest.getCartNumber());
		cartRequestDto.setQuantity(cartRequest.getQuantity());
		cartRequestDto.setCartSelected(cartRequest.isCartSelected());
		
		return cartRequestDto;
	}
}
