package com.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressDto {
	private Long id;
	private String street;
	private String city;
	private String state;
	private String country;
	private String zipCode;

	
	
}
