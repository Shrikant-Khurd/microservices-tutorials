package com.microservice.orderservice.mapper;

import com.microservice.orderservice.dto.ProductDto;
import com.microservice.orderservice.model.OrderLineItems;

public class ProductMapper {

	 // Convert OrderLineItems JPA Entity into ProductDto
	public static ProductDto mapToOrderLineProductDto(OrderLineItems orderRequest) {
		ProductDto orderLineItems = new ProductDto();
		orderLineItems.setProductId(orderRequest.getProductId());
		orderLineItems.setQuantity(orderRequest.getQuantity());
		orderLineItems.setPrice(orderRequest.getProductPrice());
		orderLineItems.setAmount(orderRequest.getAmount());
		return orderLineItems;
	}

	// Convert ProductDto into OrderLineItems JPA Entity
	public static OrderLineItems mapToOrderLineProducts(ProductDto orderRequest) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setProductId(orderRequest.getProductId());
		orderLineItems.setQuantity(orderRequest.getQuantity());
		orderLineItems.setProductPrice(orderRequest.getPrice());
		orderLineItems.setAmount(orderRequest.getAmount());
		return orderLineItems;
	}

}
