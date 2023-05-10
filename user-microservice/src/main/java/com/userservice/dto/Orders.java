package com.userservice.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
	private long id;
	private String orderNumber;
	private long userId;
	private Instant orderDate;
	private String orderStatus;
	private long totalBill;
	private List<ProductDto> orderLineItemsList;
}
