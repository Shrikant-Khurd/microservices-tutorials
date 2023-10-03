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

import com.microservice.orderservice.dto.AddressDto;
import com.microservice.orderservice.dto.ApiResponse;
import com.microservice.orderservice.dto.InventoryDto;
import com.microservice.orderservice.dto.OrderRequestDto;
import com.microservice.orderservice.dto.ProductDto;
import com.microservice.orderservice.dto.ResponseDto;
import com.microservice.orderservice.dto.UserDto;
import com.microservice.orderservice.exception.OrderNotFoundException;
import com.microservice.orderservice.feign.inventoryservice.clients.InventoryFeignClient;
import com.microservice.orderservice.feign.productservice.clients.ProductFeignClient;
import com.microservice.orderservice.feign.usercartservice.clients.UserCartFeignClient;
import com.microservice.orderservice.feign.userservice.clients.UserFeignClient;
import com.microservice.orderservice.mapper.OrderMapper;
import com.microservice.orderservice.mapper.ProductMapper;
import com.microservice.orderservice.model.OrderLineItems;
import com.microservice.orderservice.model.Orders;
import com.microservice.orderservice.repository.OrdersRepository;
import com.microservice.orderservice.util.ConstantMethods;

@Service
//@RequiredArgsConstructor
@Transactional
public class OrderService {

	private OrdersRepository ordersRepository;

	private UserFeignClient userFeignClient;

	private UserCartFeignClient usercartFeignClient;

	private ProductFeignClient productFeignClient;

	private InventoryFeignClient inventoryFeignClient;

	private MessageSource messageSource;

	public static long CURRENT_USER_ID;

	@Autowired
	public OrderService(OrdersRepository ordersRepository, UserFeignClient userFeignClient,
			UserCartFeignClient usercartFeignClient, ProductFeignClient productFeignClient,
			InventoryFeignClient inventoryFeignClient, MessageSource messageSource) {
		super();
		this.ordersRepository = ordersRepository;
		this.userFeignClient = userFeignClient;
		this.usercartFeignClient = usercartFeignClient;
		this.productFeignClient = productFeignClient;
		this.inventoryFeignClient = inventoryFeignClient;
		this.messageSource = messageSource;

		UserDto userDto = userFeignClient.getUserDetailByUserId();
		CURRENT_USER_ID = userDto.getId();
	}

	public ApiResponse placeOrders(OrderRequestDto orderRequest, boolean isSingleProductCheckout) {
		Orders order = new Orders();
		UserDto userDto = userFeignClient.getUserDetailByUserId();
		order.setUserId(userDto.getId());

		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderDate(Instant.now());
		order.setShippingAddressId(orderRequest.getShippingAddress().getId());

		order.setOrderStatus("CREATED");
		// method reference
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineProductList().stream()
				.map(ProductMapper::mapToOrderLineProducts).toList();

		double totalBill = 0;
		for (OrderLineItems product : orderLineItems) {
			ProductDto productDto = productFeignClient.getProductDetailByProductId(product.getProductId());

			double productPrice = productDto.getPrice();
			double amount = productPrice * product.getQuantity();
			product.setTotal(amount);
			product.setProductPrice(productPrice);
			totalBill = totalBill + product.getTotal();
			ProductDto items = ProductMapper.mapToOrderLineProductDto(product);
			items.setProductName(productDto.getProductName());
			items.setDescription(productDto.getDescription());
			items.setCategory(productDto.getCategory());
		}
		order.setTotalBill(totalBill);
		order.setOrderLineItemsList(orderLineItems);

		// check product quantity in inventory
		// if product quantity is less than required quantity of product then throw
		// exception msg:
//		for (OrderLineItems product : orderLineItems) {
//			InventoryDto inventory = inventoryFeignClient.productInventoryDetails(product.getProductId());
//
//		}

		Orders orderSaved = ordersRepository.save(order);
		if (!isSingleProductCheckout) {
			for (OrderLineItems product : orderLineItems) {
				ProductDto productDto = productFeignClient.getProductDetailByProductId(product.getProductId());
				long productId = productDto.getProductId();
				usercartFeignClient.removeCartByProductId(productId);
			}
		}

		List<ProductDto> orderLineItems1 = orderSaved.getOrderLineItemsList().stream()
				.map(ProductMapper::mapToOrderLineProductDto).toList();

		if (orderSaved.getOrderStatus().equals("CREATED")) {
			for (ProductDto product : orderLineItems1) {

				InventoryDto inventory = inventoryFeignClient.productInventoryDetails(product.getProductId());
				if (inventory.getProductId() == product.getProductId()) {

					int inventoryStockQuantity = inventory.getQuantityInStock();
					inventoryStockQuantity = (int) (inventoryStockQuantity - product.getQuantity());
					inventory.setQuantityInStock(inventoryStockQuantity);
					inventoryFeignClient.updateProductQuantityStock(inventory);
				}
			}
		}

		List<ProductDto> productDto1 = new ArrayList<>();
		for (ProductDto product : orderLineItems1) {
			ProductDto productDto = productFeignClient.getProductDetailByProductId(product.getProductId());

			product.setProductName(productDto.getProductName());
			product.setDescription(productDto.getDescription());
			product.setImage(productDto.getImage());
			product.setCategory(productDto.getCategory());
			productDto1.add(product);
		}

		AddressDto response = userFeignClient.getAddressDetailByAddressId(order.getShippingAddressId());

		OrderRequestDto orderRequestDto = OrderMapper.mapToOrders(orderSaved);
		orderRequestDto.setOrderLineProductList(productDto1);
		orderRequestDto.setShippingAddress(response);

		List<OrderRequestDto> users1 = new ArrayList<>();
		users1.add(orderRequestDto);

		ResponseDto dto = new ResponseDto();
		dto.setUserDetail(userDto);
		dto.setOrderDetail(users1);

		return ConstantMethods.successResponse(dto,
				messageSource.getMessage("api.response.order.placed.successfully", null, Locale.ENGLISH));
	}

	public OrderRequestDto getOrder(Long orderId) {
		Orders order = ordersRepository.findById(orderId).get();
		OrderRequestDto orderRequestDto = OrderMapper.mapToOrders(order);

		List<ProductDto> orderLineItems = order.getOrderLineItemsList().stream()
				.map(ProductMapper::mapToOrderLineProductDto).toList();

		for (ProductDto product : orderLineItems) {
			ProductDto productDto = productFeignClient.getProductDetailByProductId(product.getProductId());
			product.setProductName(productDto.getProductName());
			product.setDescription(productDto.getDescription());
			product.setImage(productDto.getImage());
			product.setCategory(productDto.getCategory());
		}
		orderRequestDto.setOrderLineProductList(orderLineItems);
		AddressDto response = userFeignClient.getAddressDetailByAddressId(order.getShippingAddressId());
		orderRequestDto.setShippingAddress(response);
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
		List<Orders> list = ordersRepository.findOrdersByUserId(userId);
		ResponseDto dto = new ResponseDto();
		List<OrderRequestDto> users1 = new ArrayList<>();
		for (Orders orderDetail : list) {
			OrderRequestDto requestDto = getOrder(orderDetail.getId());
			users1.add(requestDto);
		}
		UserDto userDto = userFeignClient.getUserDetailByUserId();
		dto.setUserDetail(userDto);
		dto.setOrderDetail(users1);
		return dto;
	}

	public ApiResponse getOrderByUserId() {
		List<Orders> list = ordersRepository.findOrdersByUserId(CURRENT_USER_ID);

		if (list.isEmpty()) {
			return ConstantMethods.successResponse(null,
					messageSource.getMessage("api.error.order.empty", null, Locale.ENGLISH));
		}

		List<OrderRequestDto> orders = new ArrayList<>();
		for (Orders orderDetail : list) {
			OrderRequestDto requestDto = getOrder(orderDetail.getId());
			orders.add(requestDto);
		}
		return ConstantMethods.successResponse(orders,
				messageSource.getMessage("api.response.order.detail", null, Locale.ENGLISH));
	}
}
