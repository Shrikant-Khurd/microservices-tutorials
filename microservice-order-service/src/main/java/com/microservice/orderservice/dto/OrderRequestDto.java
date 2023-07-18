package com.microservice.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.microservice.orderservice.model.OrderLineItems;

import jakarta.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class OrderRequestDto {
	private Long orderId;
	private long userId;
	//private UserDto userDetail;
	private Instant orderDate;
	private String orderStatus;
	private List<ProductDto> orderLineProductList;
	private double totalBill;
	private long userAddressId;
	
	private AddressDto userAddress;

	
	private Object data;
	
}
