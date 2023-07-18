package com.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	private long productId;
    private String productName;
    private String description;
    private long price;
    private long quantity;
	private long amount;
	
	private long createdByUserId;
	private UserDto createdByUser;
	
	private byte[] image;
}