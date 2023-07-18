package com.microservice.usercartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.microservice.usercartservice.model.CartLineItems;

import jakarta.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class UserCartRequestDto {
	private Long cartId;
	private long userId;
	//private UserDto userDetail;
	private Instant orderDate;
	private String orderStatus;
	private List<ProductDto> cartLineProductList;
	private long totalBill;

	
}
