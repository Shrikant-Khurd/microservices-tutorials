package com.microservice.usercartservice.mapper;

import com.microservice.usercartservice.dto.ProductDto;
import com.microservice.usercartservice.model.CartLineItems;

public class ProductMapper {

	 // Convert OrderLineItems JPA Entity into ProductDto
	public static ProductDto mapToOrderLineProductDto(CartLineItems orderRequest) {
		ProductDto orderLineItems = new ProductDto();
		orderLineItems.setProductId(orderRequest.getProductId());
		orderLineItems.setQuantity(orderRequest.getQuantity());
		orderLineItems.setPrice(orderRequest.getProductPrice());
		orderLineItems.setAmount(orderRequest.getAmount());
		return orderLineItems;
	}

	// Convert ProductDto into OrderLineItems JPA Entity
	public static CartLineItems mapToOrderLineProducts(ProductDto orderRequest) {
		CartLineItems orderLineItems = new CartLineItems();
		orderLineItems.setProductId(orderRequest.getProductId());
		orderLineItems.setQuantity(orderRequest.getQuantity());
		orderLineItems.setProductPrice(orderRequest.getPrice());
		orderLineItems.setAmount(orderRequest.getAmount());
		return orderLineItems;
	}

}
