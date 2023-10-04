package com.smartshopper.orderservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartshopper.orderservice.dto.AddressDto;
import com.smartshopper.orderservice.dto.ApiResponse;
import com.smartshopper.orderservice.dto.InventoryDto;
import com.smartshopper.orderservice.dto.OrderRequestDto;
import com.smartshopper.orderservice.dto.ProductDto;
import com.smartshopper.orderservice.dto.ResponseDto;
import com.smartshopper.orderservice.dto.UserDto;
import com.smartshopper.orderservice.exception.OrderNotFoundException;
import com.smartshopper.orderservice.feign.inventoryservice.clients.InventoryFeignClient;
import com.smartshopper.orderservice.feign.productservice.clients.ProductFeignClient;
import com.smartshopper.orderservice.feign.usercartservice.clients.UserCartFeignClient;
import com.smartshopper.orderservice.feign.userservice.clients.UserFeignClient;
import com.smartshopper.orderservice.mapper.OrderMapper;
import com.smartshopper.orderservice.mapper.ProductMapper;
import com.smartshopper.orderservice.model.OrderLineItems;
import com.smartshopper.orderservice.model.Orders;
import com.smartshopper.orderservice.repository.OrdersRepository;
import com.smartshopper.orderservice.util.ConstantMethods;

@Service
//@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrdersRepository ordersRepository;

	private final UserFeignClient userFeignClient;

	private final UserCartFeignClient usercartFeignClient;

	private final ProductFeignClient productFeignClient;

	private final InventoryFeignClient inventoryFeignClient;

	private final MessageSource messageSource;

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

		
	}
	
	

	public ApiResponse placeOrders( OrderRequestDto orderRequest, boolean isSingleProductCheckout) {
		Orders order = new Orders();
		UserDto userDto = userFeignClient.getUserDetailByUserId();
		order.setUserId(userDto.getId());

		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderDate(LocalDateTime.now());
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

	public ApiResponse getAllOrderByUserId(long userId) {
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
		return ConstantMethods.successResponse(orderRequestDtoList,
				messageSource.getMessage("api.response.order.detail", null, Locale.ENGLISH));
	}

	public ApiResponse getOrderByUserId() {
		UserDto userDto = userFeignClient.getUserDetailByUserId();
		List<Orders> list = ordersRepository.findOrdersByUserId(userDto.getId());

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
