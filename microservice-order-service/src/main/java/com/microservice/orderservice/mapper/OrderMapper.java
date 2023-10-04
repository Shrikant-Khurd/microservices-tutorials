package com.microservice.orderservice.mapper;

import com.microservice.orderservice.dto.OrderRequestDto;
import com.microservice.orderservice.model.Orders;

public class OrderMapper {

	public static OrderRequestDto mapToOrders(Orders orderRequest) {
		OrderRequestDto orderRequestDto = new OrderRequestDto();
		orderRequestDto.setOrderId(orderRequest.getId());
		orderRequestDto.setUserId(orderRequest.getUserId());
		orderRequestDto.setOrderDate(orderRequest.getOrderDate());
		orderRequestDto.setOrderStatus(orderRequest.getOrderStatus());
		orderRequestDto.setTotalBill(orderRequest.getTotalBill());
		return orderRequestDto;
	}
}