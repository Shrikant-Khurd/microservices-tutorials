package com.microservice.inventoryservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_EMPTY)
public class InventoryResponseDto {
	

	private long productId;
//    private Map<String, Integer> sizeAndQuantity;    
	private ProductDto product;
//    private Integer quantityInStock;
//    private String size;
    private boolean inventoryStatus;
    private List<StockDto> sizeAndQuantity;
    

}
