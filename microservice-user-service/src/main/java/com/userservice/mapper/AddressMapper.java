package com.userservice.mapper;

import com.userservice.dto.AddressDto;
import com.userservice.model.Address;

public class AddressMapper {
	
	 // Convert Address JPA Entity into AddressDto
	public static AddressDto mapToAddressDto(Address address) {
		AddressDto addressDto = new AddressDto();
		addressDto.setId(address.getId());
		addressDto.setStreet(address.getStreet());
		addressDto.setCity(address.getCity());
		addressDto.setZipCode(address.getZipCode());
		addressDto.setState(address.getState());
		addressDto.setCountry(address.getCountry());
		return addressDto;
	}
	
	// Convert AddressDto into Address JPA Entity
	public static Address mapToAddress(AddressDto address) {
		Address addressDto = new Address();
		addressDto.setStreet(address.getStreet());
		addressDto.setCity(address.getCity());
		addressDto.setZipCode(address.getZipCode());
		addressDto.setState(address.getState());
		addressDto.setCountry(address.getCountry());
		return addressDto;
	}
}
