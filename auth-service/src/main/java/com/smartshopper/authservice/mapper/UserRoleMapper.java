package com.smartshopper.authservice.mapper;

import com.smartshopper.authservice.dto.UserRoleDto;
import com.smartshopper.authservice.model.UserRole;

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
