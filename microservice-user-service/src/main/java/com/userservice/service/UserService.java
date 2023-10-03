package com.userservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.management.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.userservice.dto.AddressDto;
import com.userservice.dto.ApiResponse;
import com.userservice.dto.OrderRequestDto;
import com.userservice.dto.ProductDto;
import com.userservice.dto.ResponseDto;
import com.userservice.dto.UserDto;
import com.userservice.exception.EmailAlreadyExistException;
import com.userservice.exception.InstanceNotAvailableException;
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
	@Autowired
	private DiscoveryClient discoveryClient;
	// @Autowired
	// private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User getUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	public ApiResponse addUser(UserDto user) throws EmailAlreadyExistException{
		log.info("add user method called");
		//try {
		if (getUserByEmail(user.getEmail()) == null) {
			User userDetail = UserMapper.mapToUser(user);
			userDetail.setId(sequenceGenaratorService.generateSequence(User.SEQUENCE_NAME));
			// userDetail.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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

			UserDto userDto = UserMapper.mapToUserDto(savedUser);
			ApiResponse successResponse = ConstantMethods.successResponse(userDto,
					messageSource.getMessage("api.response.user.registered.successfully", null, Locale.ENGLISH));
			return successResponse;
		} 
		else {
			throw new EmailAlreadyExistException(
					messageSource.getMessage("api.error.user.already.registered", null, Locale.ENGLISH));
		}
		/*}catch (EmailAlreadyExistException e) {
			
			System.out.println("dfsfssj");
			ApiResponse failResponse = ConstantMethods.failureResponse(e.getMessage(),HttpStatus.CONFLICT);
			
			return failResponse;
		}*/
		
	}

	public ApiResponse getUserDetailAndOrders(Long userId) {
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

		ApiResponse successResponse = ConstantMethods.successResponse(responseDto,
				messageSource.getMessage("api.response.user.get.successfully", null, Locale.ENGLISH));
		return successResponse;

	}

	public UserDto getUserDetail(Long userId) {
		log.info("get user details method called");
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
				messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH)));
		return UserMapper.mapToUserDto(user);
	}

	public ApiResponse getByUserId(Long userId) {
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

	public ApiResponse getOrderDetailByUserId1(long userId) {
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

	public ApiResponse getOrderDetailByUserId(long userId) {
		try {
			OrderRequestDto[] orderResponse = restTemplate.getForObject(
					"http://order-service/api/order/get-orderDetail-userId/" + userId, OrderRequestDto[].class);
			List<OrderRequestDto> users1 = new ArrayList<>();

			for (OrderRequestDto requestDto : orderResponse) {
				users1.add(requestDto);
			}
			ApiResponse successResponse = ConstantMethods.successResponse(users1,
					messageSource.getMessage("api.response.order.detail", null, Locale.ENGLISH));
			return successResponse;
		} catch (HttpClientErrorException ex) {
			HttpStatusCode statusCode = ex.getStatusCode();
			String errorMessage;

			if (statusCode.is4xxClientError() || statusCode.is5xxServerError()) {
				ResponseEntity<String> errorResponse = ResponseEntity.status(statusCode)
						.body(ex.getResponseBodyAsString());
				errorMessage = "An error occurredddd: " + errorResponse.getBody();

			} else {
				errorMessage = "An error occurred: " + ex.getMessage();
			}

			ApiResponse errorResponse = ConstantMethods.failureResponse(errorMessage);
			return errorResponse;
		}
	}

//	public ApiResponse getOrderDetailByUserId(long userId) {
//		try {
//			OrderRequestDto[] orderResponse = restTemplate.getForObject(
//					"http://order-service/api/order/get-orderDetail-userId/" + userId, OrderRequestDto[].class);
//			List<OrderRequestDto> users1 = new ArrayList<>();
//
//			for (OrderRequestDto requestDto : orderResponse) {
//				users1.add(requestDto);
//			}
//			ApiResponse successResponse = ConstantMethods.successResponse(users1,
//					messageSource.getMessage("api.response.order.detail", null, Locale.ENGLISH));
//			return successResponse;
//		} catch (HttpClientErrorException ex) {
//			HttpStatusCode statusCode = ex.getStatusCode();
//
//			if (statusCode == HttpStatus.NOT_FOUND) {
//				// Handle the case when the requested resource is not found
//				String errorMessage = "Order detail not found for user ID: " + userId;
//
//				throw new EmailAlreadyExistException(errorMessage);
//
//			}
//		}
//		return null;
//	}

	public ApiResponse getOrderByUserId(long userId) {
		UserDto userDto = getUserDetail(userId);

		List<ServiceInstance> instances = discoveryClient.getInstances("order-service");

		if (instances.isEmpty()) {
			// Handle the case when no instances are available for order-service
			String errorMessage = messageSource.getMessage("api.error.order.instance", null, Locale.ENGLISH);
			throw new InstanceNotAvailableException(errorMessage);
		}
		// OrderDto
		ResponseDto orderResponse = restTemplate
				.getForObject("http://order-service/api/order/get-order-userId/" + userId, ResponseDto.class);
		

		ResponseDto dto = new ResponseDto();

		dto.setUser(userDto);
		// dto.setOrderDetail(orderResponse);
		// dto.setAddresses(responseDto.getAddresses());
		dto.setOrderDetail(orderResponse.getOrderDetail());
		return ConstantMethods.successResponse(dto, messageSource
				.getMessage("api.response.user.get.order.details" , null, Locale.ENGLISH) + userDto.getFirstName());
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

	public ApiResponse addUserAddress(AddressDto address, long id) {
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
			return ConstantMethods.successResponse(userDto,
					messageSource.getMessage("api.response.user.add.address", null, Locale.ENGLISH));
		}
		return null;
	}

	public ApiResponse getAddressesByUserId(long userId) {
		User userDetail = userRepository.findById(userId).get();
		List<AddressDto> addressList = new ArrayList<>();

		for (Address address : userDetail.getAddresses()) {
			AddressDto addressDto = AddressMapper.mapToAddressDto(address);
			addressList.add(addressDto);
		}
		return ConstantMethods.successResponse(addressList,
				messageSource.getMessage("api.response.user.all.address", null, Locale.ENGLISH) + " " + userId
						+ ", Name : " + userDetail.getFirstName());

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
		Address addressDetails = addressRepository.findById(addressId).get();
		// return AddressMapper.mapToAddressDto(addressDetails);
		AddressDto addressDto = AddressMapper.mapToAddressDto(addressDetails);
	//	return addressDto;
		return ConstantMethods.successResponse(addressDto,
				messageSource.getMessage("api.response.user.address.detail", null, Locale.ENGLISH) + " " + addressId);
	}
//	public AddressDto getAddressDetailByAddress(long addressId) {
//		Address addressDetails = addressRepository.findById(addressId).get();
//		// return AddressMapper.mapToAddressDto(addressDetails);
//		AddressDto addressDto = AddressMapper.mapToAddressDto(addressDetails);
//		return addressDto;
//	}

	public ApiResponse updateUserAddress(long userId,AddressDto updatedAddressDto, long addressId) {

		User userDetail = userRepository.findById(userId).orElse(null);
		if (userDetail == null) {
			throw new ResourceNotFoundException(
					messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH));
		}
		
		boolean addressUpdated  = false;
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
			
			ApiResponse addressDto= getAddressesByUserId(userId);
		    return ConstantMethods.successResponse(addressDto,
					messageSource.getMessage("api.response.user.update.address", null, Locale.ENGLISH) + " " + addressId);
		
	}

	public ApiResponse deleteAddress(long userId, long addressId) {
		User userDetail = userRepository.findById(userId).orElse(null);
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
}
