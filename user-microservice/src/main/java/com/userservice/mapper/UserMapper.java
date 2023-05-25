package com.userservice.mapper;

import com.userservice.dto.UserDto;
import com.userservice.model.User;

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
		userDto.setAccountStatus(user.isAccountStatus());
		userDto.setRoles(user.getRoles());
		// userDto.setAddresses(user.getAddresses());
		return userDto;
	}

	// Convert UserDto into User JPA Entity
	public static User mapToUser(UserDto user) {
		User userDto = new User();
		//userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setPassword(user.getPassword());
		userDto.setContactNumber(user.getContactNumber());
		userDto.setRoles(user.getRoles());
		userDto.setAddresses(user.getAddresses());
		return userDto;
	}
}
