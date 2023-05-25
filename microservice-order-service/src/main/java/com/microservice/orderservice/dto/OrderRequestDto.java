package com.microservice.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

import com.microservice.orderservice.model.OrderLineItems;

import jakarta.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
	private Long orderId;
	private long userId;
	//private UserDto userDetail;
	private Instant orderDate;
	private String orderStatus;
	private List<ProductDto> orderLineProductList;
	private long totalBill;
	private long userAddressId;
	
	private AddressDto userAddress;
}
