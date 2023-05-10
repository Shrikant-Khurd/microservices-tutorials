package com.microservice.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
	private long id;
    private long productId;
    private long quantity;
    private long productPrice;
    private long amount;
}
