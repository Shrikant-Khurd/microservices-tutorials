package com.userservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.userservice.dto.DepartmentDto;
import com.userservice.dto.OrderRequestDto;
import com.userservice.dto.ResponseDto;
import com.userservice.dto.UserDto;
import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import com.userservice.util.SequenceGenaratorService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private SequenceGenaratorService sequenceGenaratorService;

	public User addUser(User user) {
		log.info("add user method called");
		user.setId(sequenceGenaratorService.generateSequence(User.SEQUENCE_NAME));
		return userRepository.save(user);
	}

	private UserDto mapToUser(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setDepartmentId(user.getDepartmentId());
		return userDto;
	}

	public ResponseDto getUser(Long userId) {
		log.info("get user details method called");
		ResponseDto responseDto = new ResponseDto();
		User user = userRepository.findById(userId).get();
		UserDto userDto = mapToUser(user);

		DepartmentDto departmentDto = restTemplate.getForObject(
				"http://department-service/api/department/byId/" + user.getDepartmentId(), DepartmentDto.class);

		responseDto.setUser(userDto);
		userDto.setDepartment(departmentDto);
		//responseDto.setDepartment(departmentDto);
		return responseDto;
	}

	public UserDto getUserDetail(Long userId) {
		log.info("get user details method called");

		User user = userRepository.findById(userId).get();
		UserDto userDto = mapToUser(user);

		DepartmentDto departmentDto = restTemplate.getForObject(
				"http://department-service/api/department/byId/" + user.getDepartmentId(), DepartmentDto.class);

		userDto.setDepartmentId(user.getDepartmentId());
		userDto.setDepartment(departmentDto);
		return userDto;
	}

	public List<User> getAllUsers() {
		log.info("get all users method called");
		return userRepository.findAll();
	}

	public List<UserDto> getAllUsersWithDepartments() {
		List<User> userList = userRepository.findAll();
		List<UserDto> users = new ArrayList<>();
		for (User user : userList) {
			String departmentUrl = "http://department-service/api/department/byId/" + user.getDepartmentId();
			DepartmentDto departmentDto = restTemplate.getForObject(departmentUrl, DepartmentDto.class);

//			String departmentUrl = "http://department-service/api/department/department-name/" + "engineering";
//			DepartmentDto departmentDto = restTemplate.getForObject(departmentUrl, DepartmentDto.class);
//			if (user.getDepartmentId() == departmentDto.getDepartmentId()) {

			/*
			 * if (user.getDepartmentId() == 6) { UserDto userDto = mapToUser(user);
			 * userDto.setDepartment(departmentDto); users.add(userDto); }
			 */
			UserDto userDto = mapToUser(user);
			// userDto.setDepartment(departmentDto);
			// if (userDto.getDepartment().getDepartmentId()==6) {
			userDto.setDepartment(departmentDto);
			users.add(userDto);
			// }
		}
		return users;
	}

	public List<ResponseDto> getAllUsersWithDepartment() {

		List<User> users = userRepository.findAll();
		List<ResponseDto> responseDtos = new ArrayList<>();

		for (User user : users) {
//			String departmentUrl = "http://localhost:8181/api/department/byId/" + user.getDepartmentId();
			String departmentUrl = "http://department-service/api/department/byId/" + user.getDepartmentId();
			DepartmentDto department = restTemplate.getForObject(departmentUrl, DepartmentDto.class);

			ResponseDto responseDto = new ResponseDto();
			UserDto userDto = mapToUser(user);
			responseDto.setUser(userDto);
			//responseDto.setDepartment(department);
			responseDtos.add(responseDto);
		}

		return responseDtos;
	}

	public User updateUser(long userId, User user) {
		log.info("update user method called");
		User getUser = userRepository.findById(userId).get();
		getUser.setFirstName(user.getFirstName());
		getUser.setLastName(user.getLastName());
		getUser.setEmail(user.getEmail());
		getUser.setDepartmentId(user.getDepartmentId());
		log.info("department is updated");
		return userRepository.save(getUser);
	}

	public void deleteUser(long userId) {
		log.info("delete user method called");
		userRepository.deleteById(userId);
	}

	public OrderRequestDto placeOrder(OrderRequestDto orderRequest) {
		OrderRequestDto orderResponse = restTemplate.postForObject("http://order-service/api/order/add-order",
				orderRequest, OrderRequestDto.class);
		return orderResponse;
	}

	public OrderRequestDto getOrderById(Long orderId) {

		OrderRequestDto orderRequestDto = restTemplate
				.getForObject("http://order-service/api/order/order-by-id/" + orderId, OrderRequestDto.class);


		UserDto userDto = getUserDetail(orderRequestDto.getUserId());
		//orderRequestDto.setUserDetail(userDto);
		return orderRequestDto;
	}

//	public UserService(RestTemplateBuilder restTemplateBuilder) {
//		this.restTemplate = restTemplateBuilder.build();
//		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
//		messageConverters.add(converter);
//		this.restTemplate.setMessageConverters(messageConverters);
//	}

	public List<UserDto> getDepartmentByDepartmentName(String departmentName) {

		List<User> userList = userRepository.findAll();
		List<UserDto> users = new ArrayList<>();

	//	String departmentUrl = "http://department-service/api/department/department-name/" + departmentName;
		//DepartmentDto departmentDto = restTemplate.getForObject(departmentUrl, DepartmentDto.class);

		
		DepartmentDto dto= getDepartmentByDepartName(departmentName);
		for (User user : userList) {
			if (user.getDepartmentId() == dto.getDepartmentId()) {
				UserDto userDto = mapToUser(user);
				 userDto.setDepartment(dto);
				users.add(userDto);
			}
		}
		return users;
	}

	public DepartmentDto getDepartmentByDepartName(String departmentName) {
		String departmentUrl = "http://department-service/api/department/department-name/" + departmentName;
		DepartmentDto departmentDto = restTemplate.getForObject(departmentUrl, DepartmentDto.class);
		return departmentDto;
	}

	public ResponseDto placeOrders(OrderRequestDto orderRequest) {
		
		ResponseDto orderResponse = restTemplate.postForObject("http://order-service/api/order/add",
				orderRequest, ResponseDto.class);
		 
		ResponseDto dto=new ResponseDto();
		dto.setUser(orderResponse.getUser());
		dto.setOrderDetail(orderResponse.getOrderDetail());
		return dto;
	}

	public ResponseDto getOrderByUserId(long userId) {
		ResponseDto orderResponse = restTemplate.getForObject("http://order-service/api/order/get-order-userId/"+userId, ResponseDto.class);
		ResponseDto dto = new ResponseDto();
		
		ResponseDto responseDto= getUser(userId);
		
		//dto.setDepartment(responseDto.getDepartment());
		dto.setUser(responseDto.getUser());
		
		//dto.setUser(orderResponse.getUser());
		dto.setOrderDetail(orderResponse.getOrderDetail());
		return dto;
	}

	
}
