package com.smartshopper.usercartservice.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class UserCartRequestDto {
	private Long cartId;
	private long userId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime cartDate;
	private String cartNumber;
	private ProductDto cartLineProduct;
	private long quantity;
	private boolean isCartSelected;


}
