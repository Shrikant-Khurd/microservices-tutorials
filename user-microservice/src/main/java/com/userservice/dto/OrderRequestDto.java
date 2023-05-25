package com.userservice.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class OrderRequestDto {
	private Long orderId;
	private long userId;
	private Instant orderDate;
	private String orderStatus;
	private List<ProductDto> orderLineProductList;
	private long totalBill;
	private long userAddressId;
	
	private AddressDto userAddress;
}
