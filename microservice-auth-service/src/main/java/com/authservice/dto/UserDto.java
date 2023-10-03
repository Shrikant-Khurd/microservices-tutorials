package com.authservice.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.authservice.model.Address;
import com.authservice.model.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserDto {
	private long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private long contactNumber;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime createdAt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime updatedAt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime lastLogin;
	private Boolean accountStatus;
	private List<Address> addresses = new ArrayList<>();;
	private Set<UserRole> roles;

}