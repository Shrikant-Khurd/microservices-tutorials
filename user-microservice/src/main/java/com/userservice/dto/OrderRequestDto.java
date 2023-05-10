package com.userservice.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
//	private Long orderId;
//	private long userId;
//	private UserDto userDetail;
//	private Instant orderDate;
//	private long productId;
//	private ProductDto productDetail;
//	private String orderStatus;
	private Long orderId;
	private long userId;
	private Instant orderDate;
	private String orderStatus;
	
	private List<ProductDto> orderLineProductList;
	private long totalBill;
}
