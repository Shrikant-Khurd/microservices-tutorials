package com.authservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.authservice.dto.LoginRequest;
import com.authservice.dto.LoginResponse;
import com.authservice.dto.UserDto;
import com.authservice.dto.UserRoleDto;
import com.authservice.exception.ResourceNotFoundException;
import com.authservice.mapper.UserMapper;
import com.authservice.mapper.UserRoleMapper;
import com.authservice.model.User;
import com.authservice.model.UserRole;
import com.authservice.repository.UserRepository;
import com.authservice.repository.UserRoleRepository;
import com.authservice.security.JwtFilter;
import com.authservice.security.JwtTokenProvider;
import com.authservice.utils.ConstantMethods;
import com.authservice.utils.SequenceGenaratorService;

@Service
public class AuthJwtService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private SequenceGenaratorService sequenceGeneratorService;
	@Autowired
	private JwtTokenProvider jwtUtil;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public AuthJwtService(UserRepository userRepository, UserRoleRepository userRoleRepository,
			SequenceGenaratorService sequenceGeneratorService, JwtTokenProvider jwtUtil,
			BCryptPasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.sequenceGeneratorService = sequenceGeneratorService;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
	}

	public LoginResponse createJwtToken(LoginRequest loginRequest) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		System.out.println("Current Date and Time: " + currentDateTime);
		User user = null;
		if (ConstantMethods.isEmail(loginRequest.getEmailOrContactNumber())) {
			user = loadUserByEmail(loginRequest.getEmailOrContactNumber());
			if (user == null) {
				throw new ResourceNotFoundException("Email not found, please enter valid email.");
			}
		} else if (ConstantMethods.isContactNumber(loginRequest.getEmailOrContactNumber())) {
			user = loadUserByContactNumber(Long.parseLong(loginRequest.getEmailOrContactNumber()));
		}

		if (user != null) {
			boolean isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
			if (isPasswordMatch) {
				String newGeneratedToken = jwtUtil.generateJwtToken(user);
				user.setLastLogin(LocalDateTime.now());
				System.out.println("Current Date and Time  DB: " + user.getLastLogin());
				User u=userRepository.save(user);
				System.out.println("Current Date and Time saved in DB: " + u.getLastLogin());
				return new LoginResponse(newGeneratedToken);
			} else {
				throw new ResourceNotFoundException("Incorrect password, please enter correct password.");
			}
		} else {
			throw new ResourceNotFoundException("Mobile number not found, please enter valid mobile number.");
		}
	}

	public User loadUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User loadUserByContactNumber(long contactNumber) {
		return userRepository.findByContactNumber(contactNumber);
	}

	public UserRole currectUserRole() {
		Set<UserRole> userDetails = JwtFilter.CURRENT_USER_ROLE;
		UserRole userRole = new UserRole();
		for (UserRole role : userDetails) {
			userRole.setRoleId(role.getRoleId());
			userRole.setRoleName(role.getRoleName());
		}
		return userRole;
	}

	public void validateToken(String token) {
		jwtUtil.validateJwtToken(token);
	}

	// Role API's
	public List<UserDto> getUsersByRoleName(String roleName) {
		UserRole role = userRoleRepository.findByRoleName(roleName);
		List<User> userListByRole = userRepository.findByRolesContaining(role.getRoleId());
		List<UserDto> userList = new ArrayList<>();
		for (User users : userListByRole) {
			UserDto userDto = UserMapper.mapToUserDto(users);
			userDto.setRoles(users.getRoles());
			userList.add(userDto);
		}
		return userList;
	}

	public UserRoleDto addUserRole(UserRoleDto role) {
		UserRole userRole = UserRoleMapper.mapToUserRole(role);
		userRole.setRoleId(sequenceGeneratorService.generateSequence(UserRole.SEQUENCE_NAME));
		UserRole savedRole = userRoleRepository.save(userRole);
		return UserRoleMapper.mapToUserRoleDto(savedRole);
	}

	public UserRole getUsersByRoleId(long roleId) {
		return userRoleRepository.findById(roleId).get();
	}

	public List<UserRoleDto> getAllRoles() {
		List<UserRole> userRoles = userRoleRepository.findAll();
		List<UserRoleDto> userRoleDtos = new ArrayList<>();
		for (UserRole role : userRoles) {
			UserRoleDto userRoleDto = UserRoleMapper.mapToUserRoleDto(role);
			userRoleDtos.add(userRoleDto);
		}
		return userRoleDtos;
	}
}