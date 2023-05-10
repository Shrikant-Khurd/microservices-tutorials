package com.userservice.dto;

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
	private UserDto user;
   // private DepartmentDto department;
    private List<OrderRequestDto> orderDetail;
	
  
    
    
    
}