package com.userservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.userservice.dto.AddressDto;
import com.userservice.dto.ApiResponse;
import com.userservice.dto.OrderRequestDto;
import com.userservice.dto.ProductDto;
import com.userservice.dto.ResponseDto;
import com.userservice.dto.UserDto;
import com.userservice.exception.ResourceNotFoundException;
import com.userservice.mapper.AddressMapper;
import com.userservice.mapper.UserMapper;
import com.userservice.model.Address;
import com.userservice.model.User;
import com.userservice.model.UserRole;
import com.userservice.repository.AddressRepository;
import com.userservice.repository.UserRepository;
import com.userservice.repository.UserRoleRepository;
import com.userservice.util.ConstantMethods;
import com.userservice.util.SequenceGenaratorService;

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
	private RestTemplate restTemplate;
	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;
	@Autowired
	private MessageSource messageSource;

	public UserDto addUser(UserDto user) {
		log.info("add user method called");
		User userDetail = UserMapper.mapToUser(user);
		userDetail.setId(sequenceGenaratorService.generateSequence(User.SEQUENCE_NAME));
		userDetail.setCreatedAt(Instant.now());
		userDetail.setUpdatedAt(Instant.now());
		userDetail.setAccountStatus(true);

		List<Address> addressList = new ArrayList<>();
		for (Address address : user.getAddresses()) {
			address.setId(sequenceGenaratorService.generateSequence(Address.SEQUENCE_NAME));
			Address add = addressRepository.save(address);
			addressList.add(add);
		}
		userDetail.setAddresses(addressList);
		User savedUser = userRepository.save(userDetail);
		return UserMapper.mapToUserDto(savedUser);
	}

	public ResponseDto getUser(Long userId) {
		log.info("get user details method called");
		ResponseDto responseDto = new ResponseDto();
		User user = userRepository.findById(userId).get();

		ResponseDto orderResponse = restTemplate
				.getForObject("http://order-service/api/order/get-order-userId/" + userId, ResponseDto.class);

		UserDto userDto = UserMapper.mapToUserDto(user);
		List<AddressDto> addressList = new ArrayList<>();
		for (Address address : user.getAddresses()) {
			AddressDto addressDto = AddressMapper.mapToAddressDto(address);
			addressList.add(addressDto);
		}
		responseDto.setUser(userDto);
		responseDto.setAddresses(addressList);
		responseDto.setOrderDetail(orderResponse.getOrderDetail());
		return responseDto;
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
		userDto.setAddresses(user.getAddresses());
		ApiResponse successResponse = ConstantMethods.successResponse(userDto,
				messageSource.getMessage("api.response.user.get.successfully", null, Locale.ENGLISH));
		return successResponse;

		// return UserMapper.mapToUserDto(user);
	}

	public List<User> getAllUsers() {
		log.info("get all users method called");
		return userRepository.findAll();
	}

	public User updateUser(long userId, User user) {
		log.info("update user method called");
		User getUser = userRepository.findById(userId).get();
		getUser.setFirstName(user.getFirstName());
		getUser.setLastName(user.getLastName());
		getUser.setEmail(user.getEmail());
		log.info("department is updated");
		return userRepository.save(getUser);
	}

	public void deleteUser(long userId) {
		log.info("delete user method called");
		userRepository.deleteById(userId);
	}

	public OrderRequestDto getOrderByOrderId(Long orderId) {
		return restTemplate.getForObject("http://order-service/api/order/order-by-orderId/" + orderId,
				OrderRequestDto.class);

		// UserDto userDto = getUserDetail(orderRequestDto.getUserId());
		// orderRequestDto.setUserDetail(userDto);
//		return orderRequestDto;
	}

	public ResponseDto placeOrders(OrderRequestDto orderRequest) {
		ResponseDto orderResponse = restTemplate.postForObject("http://order-service/api/order/add-order", orderRequest,
				ResponseDto.class);

		ResponseDto dto = new ResponseDto();
		dto.setUser(orderResponse.getUser());
		dto.setOrderDetail(orderResponse.getOrderDetail());
		return dto;
	}

	public ProductDto getProductDetailByProductId(long productId) {
		return restTemplate.getForObject("http://product-service/api/product/get-product-byId/" + productId,
				ProductDto.class);
	}

	public List<OrderRequestDto> getOrderDetailByUserId(long userId) {
		OrderRequestDto[] orderResponse = restTemplate.getForObject(
				"http://order-service/api/order/get-orderDetail-userId/" + userId, OrderRequestDto[].class);
		List<OrderRequestDto> users1 = new ArrayList<>();

		for (OrderRequestDto requestDto : orderResponse) {
			users1.add(requestDto);
		}
		return users1;
	}

	public ResponseDto getOrderByUserId(long userId) {
		ResponseDto orderResponse = restTemplate
				.getForObject("http://order-service/api/order/get-order-userId/" + userId, ResponseDto.class);
		ResponseDto dto = new ResponseDto();

		ResponseDto responseDto = getUser(userId);

		dto.setUser(responseDto.getUser());
		dto.setAddresses(responseDto.getAddresses());
		dto.setOrderDetail(orderResponse.getOrderDetail());
		return dto;
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

	public UserDto addUserAddress(AddressDto address, long id) {
		User userDetail = userRepository.findById(id).get();
		if (userDetail.isAccountStatus()) {
			Address addAddress = AddressMapper.mapToAddress(address);
			addAddress.setId(sequenceGenaratorService.generateSequence(Address.SEQUENCE_NAME));

			addressRepository.save(addAddress);

			userDetail.getAddresses().add(addAddress);
			userDetail.setUpdatedAt(Instant.now());

			User newUser = userRepository.save(userDetail);
//			return UserDto.mapToUserDto(newUser);
			UserDto userDto = UserMapper.mapToUserDto(newUser);
			userDto.setAddresses(newUser.getAddresses());
			return userDto;
		}
		return null;
	}

	public List<AddressDto> getAddressesByUserId(long id) {
		User userDetail = userRepository.findById(id).get();
		List<AddressDto> addressList = new ArrayList<>();

		for (Address address : userDetail.getAddresses()) {
			AddressDto addressDto = AddressMapper.mapToAddressDto(address);
			addressList.add(addressDto);
		}
		return addressList;
	}

	public AddressDto getAddressDetailByAddressId(long addressId) {
		Address addressDetails = addressRepository.findById(addressId).get();
		return AddressMapper.mapToAddressDto(addressDetails);
//		AddressDto addressDto = AddressDto.mapToAddress(addressDetails);
		// return addressDto;
	}
}
