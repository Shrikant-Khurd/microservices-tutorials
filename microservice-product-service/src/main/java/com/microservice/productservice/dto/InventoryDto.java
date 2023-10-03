package com.microservice.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDto {
	private long productId;
//	private ProductDto product;
    private Integer quantityInStock;
    private boolean inventoryStatus;
}
