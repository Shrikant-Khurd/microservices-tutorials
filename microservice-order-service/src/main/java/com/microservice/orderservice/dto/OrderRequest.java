package com.microservice.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
	private long userId;

	private UserDto userDetail;
	private Instant orderDate;
//    private List<OrderLineItemsDto> orderLineItemsDtoList;
    private List<ProductDto> orderLineItemsDto;
}
