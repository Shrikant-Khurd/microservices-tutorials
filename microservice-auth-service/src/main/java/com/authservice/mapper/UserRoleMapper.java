package com.authservice.mapper;

import com.authservice.dto.UserDto;
import com.authservice.dto.UserRoleDto;
import com.authservice.model.User;
import com.authservice.model.UserRole;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRoleMapper {

	// Convert UserRole JPA Entity into UserRoleDto
	public static UserRoleDto mapToUserRoleDto(UserRole userRole) {
		UserRoleDto userRoleDto = new UserRoleDto();
		userRoleDto.setId(userRole.getRoleId());
		userRoleDto.setRoleName(userRole.getRoleName());

		return userRoleDto;
	}

	// Convert UserRoleDto into UserRole JPA Entity
	public static UserRole mapToUserRole(UserRoleDto userRole) {
		UserRole userRoleDto = new UserRole();
		userRoleDto.setRoleId(userRole.getId());
		userRoleDto.setRoleName(userRole.getRoleName());

		return userRoleDto;
	}
}
