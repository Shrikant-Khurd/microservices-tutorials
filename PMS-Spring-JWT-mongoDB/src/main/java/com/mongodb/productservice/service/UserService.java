package com.mongodb.productservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mongodb.productservice.dto.ApiResponse;
import com.mongodb.productservice.dto.UserDto;
import com.mongodb.productservice.exception.EmailAlreadyExistException;
import com.mongodb.productservice.exception.ResourceNotFoundException;
import com.mongodb.productservice.mapper.UserMapper;
import com.mongodb.productservice.model.User;
import com.mongodb.productservice.model.UserRole;
import com.mongodb.productservice.repository.UserRepository;
import com.mongodb.productservice.repository.UserRoleRepository;
import com.mongodb.productservice.security.JwtFilter;
import com.mongodb.productservice.utils.ConstantMethods;
import com.mongodb.productservice.utils.SequenceGenaratorService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;
	@Autowired
	private MessageSource messageSource;

	// @Autowired
	// private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User getUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	public ApiResponse addUser(UserDto user) {
		log.info("add user method called");
		if (getUserByEmail(user.getEmail()) == null) {
			User userDetail = UserMapper.mapToUser(user);
			userDetail.setId(sequenceGenaratorService.generateSequence(User.SEQUENCE_NAME));
			// userDetail.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userDetail.setCreatedAt(Instant.now());
			userDetail.setUpdatedAt(Instant.now());
			userDetail.setAccountStatus(true);

			User savedUser = userRepository.save(userDetail);

			UserDto userDto = UserMapper.mapToUserDto(savedUser);
			ApiResponse successResponse = ConstantMethods.successResponse(userDto,
					messageSource.getMessage("api.response.user.registered.successfully", null, Locale.ENGLISH));
			return successResponse;
		} else {
			throw new EmailAlreadyExistException(
					messageSource.getMessage("api.error.user.already.registered", null, Locale.ENGLISH));
		}

	}

	public UserDto getUserDetail() {
		User user = userRepository.findById(JwtFilter.CURRENT_USER_ID).orElseThrow(() -> new ResourceNotFoundException(
				messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH)));
		return UserMapper.mapToUserDto(user);
	}

	public UserDto getUserDetail(Long userId) {
		log.info("get user details method called");
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
				messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH)));
		return UserMapper.mapToUserDto(user);
	}

	public ApiResponse getById(Long userId) {
		log.info("get user details method called");
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
				messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH)));

		UserDto userDto = UserMapper.mapToUserDto(user);

		ApiResponse successResponse = ConstantMethods.successResponse(userDto,
				messageSource.getMessage("api.response.user.get.successfully", null, Locale.ENGLISH));
		return successResponse;

		// return UserMapper.mapToUserDto(user);
	}

	public ApiResponse getAllUsers() {
		log.info("get all users method called");
		List<User> allUsers = userRepository.findAll();
		ApiResponse successResponse = ConstantMethods.successResponse(allUsers,
				messageSource.getMessage("api.response.user.get.all.users", null, Locale.ENGLISH));
		return successResponse;
	}

	public void deleteUser(long userId) {
		log.info("delete user method called");
		userRepository.deleteById(userId);
	}

	public List<UserDto> getUsersByRoleName(String roleName) {
		UserRole role = userRoleRepository.findByRoleName(roleName);
		List<User> userListByRole = userRepository.findByRolesContaining(role.getId());
		List<UserDto> userList = new ArrayList<>();
		for (User users : userListByRole) {
			UserDto userDto = UserMapper.mapToUserDto(users);
			userDto.setRoles(users.getRoles());
			userList.add(userDto);
		}
		return userList;
	}

	public UserRole addUserRole(UserRole role) {
		role.setId(sequenceGenaratorService.generateSequence(UserRole.SEQUENCE_NAME));

		return userRoleRepository.save(role);
	}

	public UserRole getUsersByRoleId(long roleId) {
		UserRole role = userRoleRepository.findById(roleId).get();
		return role;
	}

}
