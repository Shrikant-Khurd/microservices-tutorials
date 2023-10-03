package com.microservice.usercartservice.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class UserCartRequestDto {
	private Long cartId;
	private long userId;
	//private UserDto userDetail;
	private Instant cartDate;
	private String cartNumber;
//	private String orderStatus;
	private ProductDto cartLineProduct;
	private long quantity;
	private boolean isCartSelected;


	
}
