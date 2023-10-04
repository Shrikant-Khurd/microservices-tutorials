package com.smartshopper.authservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "user-address")
public class Address {

	@Transient
	public static final String SEQUENCE_NAME = "userAddress_sequence";

	@Id
	private Long id;
	private String street;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private boolean defaultAddress;
	private boolean deletedAddressStatus;


}
