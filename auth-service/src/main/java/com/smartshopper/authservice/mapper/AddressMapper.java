package com.smartshopper.authservice.mapper;

import com.smartshopper.authservice.dto.AddressDto;
import com.smartshopper.authservice.model.Address;

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
		addressDto.setDefaultAddress(address.isDefaultAddress());
		addressDto.setDeletedAddressStatus(address.isDeletedAddressStatus());

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
		addressDto.setDeletedAddressStatus(address.isDeletedAddressStatus());
		return addressDto;
	}
}