package com.smartshopper.usercartservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

	private UserDto userDetail;
//	private ProductDto productDetail;
	private List<UserCartRequestDto> cartDetail;

}
