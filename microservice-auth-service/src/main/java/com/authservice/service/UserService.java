package com.authservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.authservice.dto.AddressDto;
import com.authservice.dto.ApiResponse;
import com.authservice.dto.OrderRequestDto;
import com.authservice.dto.ResponseDto;
import com.authservice.dto.UserDto;
import com.authservice.dto.UserRoleDto;
import com.authservice.exception.EmailAlreadyExistException;
import com.authservice.exception.InstanceNotAvailableException;
import com.authservice.exception.ResourceNotFoundException;
import com.authservice.mapper.AddressMapper;
import com.authservice.mapper.UserMapper;
import com.authservice.mapper.UserRoleMapper;
import com.authservice.model.Address;
import com.authservice.model.User;
import com.authservice.model.UserRole;
import com.authservice.repository.AddressRepository;
import com.authservice.repository.UserRepository;
import com.authservice.repository.UserRoleRepository;
import com.authservice.security.JwtFilter;
import com.authservice.utils.ConstantMethods;
import com.authservice.utils.SequenceGenaratorService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User getUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	public ApiResponse addUser(UserDto user) {
		log.info("add user method called");
		if (getUserByEmail(user.getEmail()) == null) {
			User userDetail = UserMapper.mapToUser(user);
			userDetail.setId(sequenceGenaratorService.generateSequence(User.SEQUENCE_NAME));
			userDetail.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userDetail.setCreatedAt(Instant.now());
			userDetail.setUpdatedAt(Instant.now());
			userDetail.setAccountStatus(true);

//			long roleId=1;
			Set<UserRole> role = userRoleRepository.findByRoleId(1);
			userDetail.setRoles(role);
//			userDetail.setRoles(user.getRoles());

			List<Address> addressList = new ArrayList<>();
			for (Address address : user.getAddresses()) {
				address.setId(sequenceGenaratorService.generateSequence(Address.SEQUENCE_NAME));
				address.setDefaultAddress(true);
				Address add = addressRepository.save(address);
				addressList.add(add);
			}
			userDetail.setAddresses(addressList);

			User savedUser = userRepository.save(userDetail);
			System.out.println(savedUser.getRoles());
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
		List<User> userListByRole = userRepository.findByRolesContaining(role.getRoleId());
		List<UserDto> userList = new ArrayList<>();
		for (User users : userListByRole) {
			UserDto userDto = UserMapper.mapToUserDto(users);
			userDto.setRoles(users.getRoles());
			userList.add(userDto);
		}
		return userList;
	}

	public UserRole addUserRole(UserRole role) {
		role.setRoleId(sequenceGenaratorService.generateSequence(UserRole.SEQUENCE_NAME));

		return userRoleRepository.save(role);
	}

	public UserRole getUsersByRoleId(long roleId) {
		UserRole role = userRoleRepository.findById(roleId).get();
		return role;
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

	// ADDRESS APIs
	public AddressDto addAddress(AddressDto address) {
		Address addAddress = AddressMapper.mapToAddress(address);
		addAddress.setId(sequenceGenaratorService.generateSequence(Address.SEQUENCE_NAME));
		Address savedAddress = addressRepository.save(addAddress);
		return AddressMapper.mapToAddressDto(savedAddress);
	}

	public List<Address> getAllAddress() {
		log.info("get all users method called");
		return addressRepository.findAll();
	}

	public ApiResponse addUserAddress(AddressDto address) {
		User userDetail = userRepository.findById(JwtFilter.CURRENT_USER_ID).get();
		if (userDetail.isAccountStatus()) {
			Address addAddress = AddressMapper.mapToAddress(address);
			addAddress.setId(sequenceGenaratorService.generateSequence(Address.SEQUENCE_NAME));
			addAddress.setDefaultAddress(false);
			Address add = addressRepository.save(addAddress);

			userDetail.getAddresses().add(add);
			userDetail.setUpdatedAt(Instant.now());

			User newUser = userRepository.save(userDetail);
//			return UserDto.mapToUserDto(newUser);
			UserDto userDto = UserMapper.mapToUserDto(newUser);
			userDto.setAddresses(newUser.getAddresses());
			return ConstantMethods.successResponse(null,
					messageSource.getMessage("api.response.user.add.address", null, Locale.ENGLISH));
		}
		return null;
	}

	public ApiResponse getAddressesByUserId() {
		User userDetail = userRepository.findById(JwtFilter.CURRENT_USER_ID).get();
		List<AddressDto> addressList = new ArrayList<>();

		for (Address address : userDetail.getAddresses()) {
			AddressDto addressDto = AddressMapper.mapToAddressDto(address);
			addressList.add(addressDto);
		}
		return ConstantMethods.successResponse(addressList,
				messageSource.getMessage("api.response.user.all.address", null, Locale.ENGLISH) + " "
						+ JwtFilter.CURRENT_USER_ID + ", Name : " + userDetail.getFirstName());

		// return addressList;
	}

	public List<AddressDto> getAddressesByUser(long userId) {
		User userDetail = userRepository.findById(userId).get();
		List<AddressDto> addressList = new ArrayList<>();

		for (Address address : userDetail.getAddresses()) {
			AddressDto addressDto = AddressMapper.mapToAddressDto(address);
			addressList.add(addressDto);
		}
		return addressList;
	}

	public ApiResponse getAddressDetailByAddressId(long addressId) {
		User userDetail = userRepository.findById(JwtFilter.CURRENT_USER_ID).get();
		AddressDto addressDto = new AddressDto();
		boolean addressFound = false;
		for (Address address : userDetail.getAddresses()) {
			if (address.getId() == addressId) {

				Address addressDetails = addressRepository.findById(addressId).get();
				addressDto = AddressMapper.mapToAddressDto(addressDetails);
				addressFound = true;
			}
		}
		if (!addressFound) {
			throw new ResourceNotFoundException(
					messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH) + addressId);
		}

		// return AddressMapper.mapToAddressDto(addressDetails);
		// return addressDto;
		return ConstantMethods.successResponse(addressDto,
				messageSource.getMessage("api.response.user.address.detail", null, Locale.ENGLISH) + " " + addressId);
	}

	public AddressDto getAddressByAddressId(long addressId) {
		System.out.println("fdfdhfdfbb");
		User userDetail = userRepository.findById(JwtFilter.CURRENT_USER_ID).get();
		AddressDto addressDto = new AddressDto();
		boolean addressFound = false;
		for (Address address : userDetail.getAddresses()) {
			if (address.getId() == addressId) {

				Address addressDetails = addressRepository.findById(addressId).get();
				addressDto = AddressMapper.mapToAddressDto(addressDetails);
				addressFound = true;
			}
		}
		if (!addressFound) {
			throw new ResourceNotFoundException(
					messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH) + addressId);
		}

		return addressDto;

	}
//	public AddressDto getAddressDetailByAddress(long addressId) {
//		Address addressDetails = addressRepository.findById(addressId).get();
//		// return AddressMapper.mapToAddressDto(addressDetails);
//		AddressDto addressDto = AddressMapper.mapToAddressDto(addressDetails);
//		return addressDto;
//	}

	public ApiResponse updateUserAddress(AddressDto updatedAddressDto, long addressId) {

		User userDetail = userRepository.findById(JwtFilter.CURRENT_USER_ID).get();
		if (userDetail == null) {
			throw new ResourceNotFoundException(
					messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH));
		}

		boolean addressUpdated = false;
		for (Address address : userDetail.getAddresses()) {
			if (address.getId() == addressId) {
				// Update the existing address with the updated values
				address.setStreet(updatedAddressDto.getStreet());
				address.setCity(updatedAddressDto.getCity());
				address.setState(updatedAddressDto.getState());
				address.setCountry(updatedAddressDto.getCountry());
				address.setZipCode(updatedAddressDto.getZipCode());

				addressRepository.save(address);
				addressUpdated = true;
			}
		}
		if (!addressUpdated) {
			throw new ResourceNotFoundException(
					messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH) + addressId);
		}
		userDetail.setUpdatedAt(Instant.now());

		User newUser = userRepository.save(userDetail);
		UserDto userDto = UserMapper.mapToUserDto(newUser);
		userDto.setAddresses(newUser.getAddresses());

		// ApiResponse addressDto = getAddressesByUserId(JwtFilter.CURRENT_USER_ID);
		return ConstantMethods.successResponse(null,
				messageSource.getMessage("api.response.user.update.address", null, Locale.ENGLISH) + " " + addressId);

	}

	public ApiResponse setAsDefaultUserAddress(long addressId) {

		User userDetail = userRepository.findById(JwtFilter.CURRENT_USER_ID).get();
		if (userDetail == null) {
			throw new ResourceNotFoundException(
					messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH));
		}

		boolean addressUpdated = false;
		for (Address address : userDetail.getAddresses()) {
			if (address.getId() == addressId) {

				address.setDefaultAddress(true);
				addressRepository.save(address);
				addressUpdated = true;
			} else if (address.getId() != addressId) {

				address.setDefaultAddress(false);
				addressRepository.save(address);
				addressUpdated = true;
			}
		}
		if (!addressUpdated) {
			throw new ResourceNotFoundException(
					messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH) + addressId);
		}
		userDetail.setUpdatedAt(Instant.now());

		User newUser = userRepository.save(userDetail);
		UserDto userDto = UserMapper.mapToUserDto(newUser);
		userDto.setAddresses(newUser.getAddresses());

		// ApiResponse addressDto = getAddressesByUserId(JwtFilter.CURRENT_USER_ID);
		return ConstantMethods.successResponse(null,
				messageSource.getMessage("api.response.user.set.default.address", null, Locale.ENGLISH));

	}

	public ApiResponse deleteAddress(long addressId) {
//		User userDetail = userRepository.findById(userId).orElse(null);
		User userDetail = userRepository.findById(JwtFilter.CURRENT_USER_ID).get();
		if (userDetail == null) {
			throw new ResourceNotFoundException(
					messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH));
		}
		boolean addressFound = false;
		List<Address> addressList = new ArrayList<>();
		for (Address address : userDetail.getAddresses()) {
			if (address.getId() == addressId) {
				addressRepository.deleteById(addressId);
				addressFound = true;
			} else {
				addressList.add(address);
			}
		}
		if (!addressFound) {
			throw new ResourceNotFoundException(
					messageSource.getMessage("api.error.address.not.found", null, Locale.ENGLISH) + addressId);
		}

		userDetail.setAddresses(addressList);
		userDetail.setUpdatedAt(Instant.now());
		User updatedUser = userRepository.save(userDetail);

		return ConstantMethods.successResponse(updatedUser,
				messageSource.getMessage("api.response.user.delete.address", null, Locale.ENGLISH) + " " + addressId);
	}

	// ORDER API
	public ResponseDto placeOrders(OrderRequestDto orderRequest) {
		ResponseDto orderResponse = restTemplate.postForObject("http://order-service/api/order/add-order", orderRequest,
				ResponseDto.class);

		ResponseDto dto = new ResponseDto();
		dto.setUser(orderResponse.getUser());
		dto.setOrderDetail(orderResponse.getOrderDetail());
		return dto;
	}

	public ApiResponse getOrderByUserId() {
		UserDto userDto = getUserDetail();
		long userId = userDto.getId();

//		List<ServiceInstance> instances = discoveryClient.getInstances("order-service");
//
//		if (instances.isEmpty()) {
//			// Handle the case when no instances are available for order-service
//			String errorMessage = messageSource.getMessage("api.error.order.instance", null, Locale.ENGLISH);
//			throw new InstanceNotAvailableException(errorMessage);
//		}
		// OrderDto
		ResponseDto orderResponse = restTemplate
				.getForObject("http://order-service/api/order/get-order-userId/" + userId, ResponseDto.class);

		ResponseDto dto = new ResponseDto();

		dto.setUser(userDto);
		// dto.setOrderDetail(orderResponse);
		// dto.setAddresses(responseDto.getAddresses());
		dto.setOrderDetail(orderResponse.getOrderDetail());
		return ConstantMethods.successResponse(dto,
				messageSource.getMessage("api.response.user.get.order.details", null, Locale.ENGLISH)
						+ userDto.getFirstName());
	}

	public ApiResponse getOrderDetailByUserId(long userId) {

		OrderRequestDto[] orderResponse = restTemplate.getForObject(
				"http://order-service/api/order/get-orderDetail-userId/" + userId, OrderRequestDto[].class);
		List<OrderRequestDto> users1 = new ArrayList<>();

		for (OrderRequestDto requestDto : orderResponse) {
			users1.add(requestDto);
		}
		ApiResponse successResponse = ConstantMethods.successResponse(users1,
				messageSource.getMessage("api.response.order.detail", null, Locale.ENGLISH));
		return successResponse;

	}

}
