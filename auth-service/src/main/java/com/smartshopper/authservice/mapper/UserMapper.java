package com.smartshopper.authservice.mapper;

import com.smartshopper.authservice.dto.UserDto;
import com.smartshopper.authservice.model.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserMapper {
	
	 // Convert User JPA Entity into UserDto
	public static UserDto mapToUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setPassword(user.getPassword());
		userDto.setContactNumber(user.getContactNumber());
		userDto.setCreatedAt(user.getCreatedAt());
		userDto.setUpdatedAt(user.getUpdatedAt());
		userDto.setLastLogin(user.getLastLogin());
		userDto.setAccountStatus(user.isAccountStatus());
		userDto.setRoles(user.getRoles());
		 userDto.setAddresses(user.getAddresses());
		return userDto;
	}

	// Convert UserDto into User JPA Entity
	public static User mapToUser(UserDto user) {
		User userDto = new User();
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setPassword(user.getPassword());
		userDto.setContactNumber(user.getContactNumber());
		userDto.setAddresses(user.getAddresses());
		return userDto;
	}
}
