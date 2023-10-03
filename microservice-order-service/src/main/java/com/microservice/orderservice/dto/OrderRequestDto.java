package com.microservice.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime orderDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime shippingDate;
	private String orderStatus;
	private List<ProductDto> orderLineProductList;
	private double totalBill;

	private AddressDto shippingAddress;

}
