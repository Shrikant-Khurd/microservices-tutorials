package com.microservice.usercartservice.mapper;

import com.microservice.usercartservice.dto.UserCartRequestDto;
import com.microservice.usercartservice.model.UserCart;

public class OrderMapper {

	public static UserCartRequestDto mapToOrders(UserCart orderRequest) {
		UserCartRequestDto orderRequestDto = new UserCartRequestDto();
		orderRequestDto.setCartId(orderRequest.getId());
		orderRequestDto.setUserId(orderRequest.getUserId());
		orderRequestDto.setOrderDate(orderRequest.getCartDate());
		orderRequestDto.setTotalBill(orderRequest.getTotalBill());
		return orderRequestDto;
	}
}
