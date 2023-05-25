package com.microservice.orderservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.microservice.orderservice.dto.AddressDto;
import com.microservice.orderservice.dto.OrderRequestDto;
import com.microservice.orderservice.dto.ProductDto;
import com.microservice.orderservice.dto.ResponseDto;
import com.microservice.orderservice.dto.UserDto;
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

	public UserDto getUserDetailByUserId(long userId) {
		return restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" + userId, UserDto.class);
	}

	public AddressDto getAddressDetailByAddressId(long addressId) {
		return restTemplate.getForObject("http://user-service/api/user/address-detail-byId/" + addressId,
				AddressDto.class);
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
		order.setOrderStatus("CREATED");
		// method reference
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineProductList().stream()
				.map(ProductMapper::mapToOrderLineProducts).toList();

		// lambda expression
//		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineProductList().stream()
//				.map(this::mapToOrderLineProducts).toList();
//		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineProductList().stream()
//				.map(productDto -> ProductMapper.mapToOrderLineProducts(productDto)).toList();
		long totalBill = 0;
		for (OrderLineItems product : orderLineItems) {
			ProductDto productDto = getProductDetailByProductId(product.getProductId());

			long productPrice = productDto.getPrice();
			long amount = productPrice * product.getQuantity();
			product.setAmount(amount);
			product.setProductPrice(productPrice);
			totalBill = totalBill + product.getAmount();
			ProductDto items = ProductMapper.mapToOrderLineProductDto(product);
			items.setName(productDto.getName());
			items.setDescription(productDto.getDescription());

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
			UserDto dto = getUserDetailByUserId(productDto.getCreatedByUserId());

			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setCreatedByUser(dto);
			productDto1.add(product);
		}

		OrderRequestDto orderRequestDto = OrderMapper.mapToOrders(order);
		orderRequestDto.setOrderLineProductList(productDto1);
//		orderRequestDto.setOrderLineProductList(orderLineItems1);
		List<OrderRequestDto> users1 = new ArrayList<>();
		users1.add(orderRequestDto);
		UserDto userDto = getUserDetailByUserId(order.getUserId());

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
			UserDto dto = getUserDetailByUserId(productDto.getCreatedByUserId());

			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setCreatedByUserId(productDto.getCreatedByUserId());
			product.setCreatedByUser(dto);
		}
		orderRequestDto.setOrderLineProductList(orderLineItems);
		AddressDto addressDto = getAddressDetailByAddressId(order.getUserAddressId());
		orderRequestDto.setUserAddress(addressDto);
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
		List<Orders> list = ordersRepository.findOrdersByUserId(userId);
		List<OrderRequestDto> users1 = new ArrayList<>();
		for (Orders orderDetail : list) {
			OrderRequestDto requestDto = getOrder(orderDetail.getId());
			users1.add(requestDto);
		}
		return users1;
	}

	public ResponseDto getOrderByUserId(long userId) {
		List<Orders> list = ordersRepository.findOrdersByUserId(userId);
		ResponseDto dto = new ResponseDto();
		List<OrderRequestDto> users1 = new ArrayList<>();
		for (Orders orderDetail : list) {
			OrderRequestDto requestDto = getOrder(orderDetail.getId());
			users1.add(requestDto);
		}
		UserDto userDto = getUserDetailByUserId(userId);
		dto.setUserDetail(userDto);
		dto.setOrderDetail(users1);
		return dto;
	}
}
