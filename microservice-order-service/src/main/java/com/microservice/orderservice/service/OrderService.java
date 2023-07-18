package com.microservice.orderservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.microservice.orderservice.dto.AddressDto;
import com.microservice.orderservice.dto.ApiResponse;
import com.microservice.orderservice.dto.OrderRequestDto;
import com.microservice.orderservice.dto.ProductDto;
import com.microservice.orderservice.dto.ResponseDto;
import com.microservice.orderservice.dto.UserDto;
import com.microservice.orderservice.exception.OrderNotFoundException;
import com.microservice.orderservice.mapper.OrderMapper;
import com.microservice.orderservice.mapper.ProductMapper;
import com.microservice.orderservice.model.OrderLineItems;
import com.microservice.orderservice.model.Orders;
import com.microservice.orderservice.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrdersRepository ordersRepository;

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private MessageSource messageSource;

	public UserDto getUserDetailByUserId() {
		return restTemplate.postForObject("http://auth-service/api/users/get-user-by-id",null, UserDto.class);
	}

//	public AddressDto getAddressDetailByAddressId(long addressId) {
//		return restTemplate.getForObject("http://user-service/api/user/address-detail/" + addressId,
//				AddressDto.class);
//	}
	public ApiResponse getAddressDetailByAddressId(long addressId) {
		return restTemplate.getForObject("http://auth-service/api/users/address-detail/" + addressId, ApiResponse.class);
	}

	public ProductDto getProductDetailByProductId(long productId) {
		return restTemplate.getForObject("http://product-service/api/product/get-product-byId/" + productId,
				ProductDto.class);
	}

	public ResponseDto placeOrders(OrderRequestDto orderRequest) {
		Orders order = new Orders();
		order.setUserId(orderRequest.getUserId());

		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderDate(Instant.now());
		order.setUserAddressId(orderRequest.getUserAddressId());

		order.setOrderStatus("PLACED");
		// method reference
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineProductList().stream()
				.map(ProductMapper::mapToOrderLineProducts).toList();

		// lambda expression
//		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineProductList().stream()
//				.map(this::mapToOrderLineProducts).toList();
//		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineProductList().stream()
//				.map(productDto -> ProductMapper.mapToOrderLineProducts(productDto)).toList();
		double totalBill = 0;
		for (OrderLineItems product : orderLineItems) {
			ProductDto productDto = getProductDetailByProductId(product.getProductId());

			double productPrice = productDto.getPrice();
			double amount = productPrice * product.getQuantity();
			product.setAmount(amount);
			product.setProductPrice(productPrice);
			totalBill = totalBill + product.getAmount();
			ProductDto items = ProductMapper.mapToOrderLineProductDto(product);
			items.setProductName(productDto.getProductName());
			items.setDescription(productDto.getDescription());
			items.setCategory(productDto.getCategory());
		}
		order.setTotalBill(totalBill);
		order.setOrderLineItemsList(orderLineItems);
//		order.setUserAddressId(userAddressId);
		ordersRepository.save(order);
//		List<ProductDto> orderLineItems1 = order.getOrderLineItemsList().stream().map(this::mapToOrderLineItems)
//				.toList();
//		List<ProductDto> orderLineItems1 = order.getOrderLineItemsList().stream()
//				.map(orderLineItems2 -> ProductMapper.mapToOrderLineItems(orderLineItems2)).toList();
		List<ProductDto> orderLineItems1 = order.getOrderLineItemsList().stream()
				.map(ProductMapper::mapToOrderLineProductDto).toList();

		List<ProductDto> productDto1 = new ArrayList<>();
		for (ProductDto product : orderLineItems1) {
			ProductDto productDto = getProductDetailByProductId(product.getProductId());

			product.setProductName(productDto.getProductName());
			product.setDescription(productDto.getDescription());
			product.setImage(productDto.getImage());
			product.setCategory(productDto.getCategory());

			productDto1.add(product);
		}

		ApiResponse response = getAddressDetailByAddressId(order.getUserAddressId());
		Object addressDto2 = response.getData();
		OrderRequestDto orderRequestDto = OrderMapper.mapToOrders(order);
		orderRequestDto.setOrderLineProductList(productDto1);
		orderRequestDto.setData(addressDto2);
		


//		orderRequestDto.setOrderLineProductList(orderLineItems1);
		List<OrderRequestDto> users1 = new ArrayList<>();
		users1.add(orderRequestDto);
//		UserDto userDto = getUserDetailByUserId(order.getUserId());
		UserDto userDto = getUserDetailByUserId();

		ResponseDto dto = new ResponseDto();
		dto.setUserDetail(userDto);
		dto.setOrderDetail(users1);
		return dto;
	}

	public OrderRequestDto getOrder(Long orderId) {
		Orders order = ordersRepository.findById(orderId).get();
		OrderRequestDto orderRequestDto = OrderMapper.mapToOrders(order);
//		List<ProductDto> orderLineItems = order.getOrderLineItemsList().stream().map(this::mapToOrderLineItems)
//				.toList();
//		List<ProductDto> orderLineItems = order.getOrderLineItemsList().stream()
//				.map(orderLineItems1 -> ProductMapper.mapToOrderLineItems(orderLineItems1)).toList();
		List<ProductDto> orderLineItems = order.getOrderLineItemsList().stream()
				.map(ProductMapper::mapToOrderLineProductDto).toList();

		for (ProductDto product : orderLineItems) {
			ProductDto productDto = getProductDetailByProductId(product.getProductId());

			product.setProductName(productDto.getProductName());
			product.setDescription(productDto.getDescription());
			product.setImage(productDto.getImage());
			product.setCategory(productDto.getCategory());


		}
		orderRequestDto.setOrderLineProductList(orderLineItems);
		// AddressDto addressDto =
		// getAddressDetailByAddressId(order.getUserAddressId());

		ApiResponse response = getAddressDetailByAddressId(order.getUserAddressId());

		Object addressDto2 = response.getData();

		// orderRequestDto.setUserAddress(addressDto);
		orderRequestDto.setData(addressDto2);
		return orderRequestDto;
	}

	public List<OrderRequestDto> getAllOrders() {
		List<Orders> order = ordersRepository.findAll();
		List<OrderRequestDto> users1 = new ArrayList<>();
		for (Orders orderDetail : order) {
			OrderRequestDto requestDto = getOrder(orderDetail.getId());
			users1.add(requestDto);
		}
		return users1;
	}

	public List<OrderRequestDto> getAllOrderByUserId(long userId) {
//		getUserDetailByUserId(userId);
		List<Orders> ordersList = ordersRepository.findOrdersByUserId(userId);
		if (ordersList.isEmpty()) {
			throw new OrderNotFoundException(
					messageSource.getMessage("api.error.order.user.not.found", null, Locale.ENGLISH));
		}

		List<OrderRequestDto> orderRequestDtoList = new ArrayList<>();
		for (Orders order : ordersList) {
			OrderRequestDto requestDto = getOrder(order.getId());
			orderRequestDtoList.add(requestDto);
		}
		return orderRequestDtoList;
	}

	public ResponseDto getOrderByUserId(long userId) {

		// Orders order =ordersRepository.findOrderByUserId(userId);
		// if(order.getUserId()!=userId) {
		// throw new
		// OrderNotFoundException(messageSource.getMessage("api.error.order.user.not.found",
		// null, Locale.ENGLISH));
		// }

		List<Orders> list = ordersRepository.findOrdersByUserId(userId);
		ResponseDto dto = new ResponseDto();
		List<OrderRequestDto> users1 = new ArrayList<>();
		for (Orders orderDetail : list) {
			OrderRequestDto requestDto = getOrder(orderDetail.getId());
			users1.add(requestDto);
		}
		UserDto userDto = getUserDetailByUserId();
//		UserDto userDto = getUserDetailByUserId(userId);
		dto.setUserDetail(userDto);
		dto.setOrderDetail(users1);
		return dto;
	}
}
