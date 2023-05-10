package com.microservice.orderservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.microservice.orderservice.dto.OrderRequestDto;
import com.microservice.orderservice.dto.ProductDto;
import com.microservice.orderservice.dto.ResponseDto;
import com.microservice.orderservice.dto.UserDto;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.model.OrderLineItems;
import com.microservice.orderservice.model.Orders;
import com.microservice.orderservice.repository.OrderRepository;
import com.microservice.orderservice.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrdersRepository ordersRepository;

	@Autowired
	private RestTemplate restTemplate;

	public Order placeOrder(Order orderRequest) {
		/*
		 * log.info("OrderService | placeOrder | Order placed, Order Id : {}",
		 * orderRequest.getId()); orderRequest.setOrderDate(Instant.now());
		 * orderRequest.setOrderStatus("CREATED");
		 * 
		 * ProductDto productDto = restTemplate.getForObject(
		 * "http://product-service/api/product/byId/" + orderRequest.getProductId(),
		 * ProductDto.class);
		 * 
		 * long productPrice = productDto.getPrice(); long amount = productPrice *
		 * orderRequest.getQuantity(); orderRequest.setAmount(amount);
		 * orderRequest.setProductPrice(productPrice);
		 */

		return orderRepository.save(orderRequest);
	}

	public UserDto getUserDetailByUserId(long userId) {
		UserDto userDetail = restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" + userId,
				UserDto.class);
		return userDetail;
	}
	public ProductDto getProductDetailByProductId(long productId) {
		ProductDto productDetail = restTemplate.getForObject(
				"http://product-service/api/product/byId/" +productId, ProductDto.class);

		return productDetail;
	}

	public ResponseDto placeOrders(OrderRequestDto orderRequest) {
		Orders order = new Orders();

		order.setUserId(orderRequest.getUserId());
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderDate(Instant.now());
		order.setOrderStatus("CREATED");

		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineProductList().stream()
				.map(this::mapToOrderLineProducts).toList();
		long total_bill = 0;
		for (OrderLineItems product : orderLineItems) {
//			ProductDto productDto = restTemplate.getForObject(
//					"http://product-service/api/product/byId/" + product.getProductId(), ProductDto.class);

			ProductDto productDto=getProductDetailByProductId(product.getProductId());
			
			long productPrice = productDto.getPrice();
			long amount = productPrice * product.getQuantity();
			product.setAmount(amount);
			product.setProductPrice(productPrice);

			total_bill = total_bill + product.getAmount();

			ProductDto items = mapToOrderLineItems(product);
			items.setName(productDto.getName());
			items.setDescription(productDto.getDescription());
			// responseDtos.add(items);

		}
		order.setTotalBill(total_bill);
		order.setOrderLineItemsList(orderLineItems);
		ordersRepository.save(order);

		List<ProductDto> orderLineItems1 = order.getOrderLineItemsList().stream().map(this::mapToOrderLineItems)
				.toList();

		for (ProductDto product : orderLineItems1) {
//			ProductDto productDto = restTemplate.getForObject(
//					"http://product-service/api/product/byId/" + product.getProductId(), ProductDto.class);
			ProductDto productDto=getProductDetailByProductId(product.getProductId());
			
			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			// mapToOrderLineProducts(user);
			// responseDtos.add(items);
		}

		OrderRequestDto orderRequestDto = mapToOrders(order);
		orderRequestDto.setOrderLineProductList(orderLineItems1);
		List<OrderRequestDto> users1 = new ArrayList<>();
		users1.add(orderRequestDto);
		UserDto userDto = restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" + order.getUserId(),
				UserDto.class);

		//orderRequestDto.setUserDetail(userDto);
		ResponseDto dto=new ResponseDto();
		dto.setUserDetail(userDto);
		dto.setOrderDetail(users1);
		
		return dto;
	}

	public ResponseDto getOrderDetails(long orderId) {
		ResponseDto responseDto = new ResponseDto();
		/*
		 * Order order = orderRepository.findById(orderId).get();
		 * 
		 * OrderRequestDto orderRequestDto = mapToOrder(order);
		 * 
		 * ProductDto productDto = restTemplate
		 * .getForObject("http://product-service/api/product/byId/" +
		 * order.getProductId(), ProductDto.class);
		 * 
		 * productDto.setQuantity(order.getQuantity());
		 * productDto.setAmount(order.getAmount());
		 * 
		 * UserDto userDto =
		 * restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" +
		 * order.getUserId(), UserDto.class);
		 * 
		 * responseDto.setOrderDetail(orderRequestDto);
		 * responseDto.setProductDetail(productDto); responseDto.setUserDetail(userDto);
		 */
		return responseDto;
	}

	/*
	 * public OrderRequestDto getOrderDetail(long orderId) { Order order =
	 * orderRepository.findById(orderId).get();
	 * 
	 * OrderRequestDto orderRequestDto = mapToOrder(order);
	 * 
	 * ProductDto productDto = restTemplate
	 * .getForObject("http://product-service/api/product/byId/" +
	 * order.getProductId(), ProductDto.class);
	 * 
	 * productDto.setQuantity(order.getQuantity());
	 * productDto.setAmount(order.getAmount());
	 * 
	 * UserDto userDto =
	 * restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" +
	 * order.getUserId(), UserDto.class);
	 * 
	 * orderRequestDto.setUserDetail(userDto); return orderRequestDto; }
	 */

	/*
	 * public OrderRequest getOrderDetailList(long orderId) { Orders order =
	 * ordersRepository.findById(orderId).get(); List<ProductDto> responseDtos = new
	 * ArrayList<>();
	 * 
	 * // OrderRequest orderRequestDto = mapToOrderLineItems(order); OrderRequest
	 * orderRequestDto = new OrderRequest();
	 * 
	 * // ProductDto productDto = //
	 * restTemplate.getForObject("http://product-service/api/product/byId/" + //
	 * order.getProductId(), ProductDto.class);
	 * 
	 * for (ProductDto user : responseDtos) { ProductDto productDto = restTemplate
	 * .getForObject("http://product-service/api/product/byId/" +
	 * user.getProductId(), ProductDto.class);
	 * 
	 * // productDto.setQuantity(order.getQuantity()); //
	 * productDto.setAmount(order.getAmount()); responseDtos.add(productDto); }
	 * UserDto userDto =
	 * restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" +
	 * order.getUserId(), UserDto.class); orderRequestDto.setUserId(order.getId());
	 * orderRequestDto.setUserDetail(userDto);
	 * orderRequestDto.setOrderLineItemsDto(responseDtos);
	 * 
	 * return orderRequestDto; }
	 */

	public OrderRequestDto getOrderById(long orderId) {
		Order order = orderRepository.findById(orderId).get();
		OrderRequestDto orderRequestDto = mapToOrder(order);
//		ProductDto productDto = restTemplate
//				.getForObject("http://product-service/api/product/byId/" + order.getProductId(), ProductDto.class);

		ProductDto productDto=getProductDetailByProductId(order.getProductId());
		
		productDto.setQuantity(order.getQuantity());
		productDto.setAmount(order.getAmount());

		// orderRequestDto.setProductDetail(productDto);
		return orderRequestDto;
	}

	private OrderRequestDto mapToOrder(Order orderRequest) {
		OrderRequestDto orderRequestDto = new OrderRequestDto();
		orderRequestDto.setOrderId(orderRequest.getId());
//		orderRequestDto.setProductId(orderRequest.getProductId());
		//orderRequestDto.setUserId(orderRequest.getUserId());
		orderRequestDto.setOrderDate(orderRequest.getOrderDate());
		orderRequestDto.setOrderStatus(orderRequest.getOrderStatus());
		return orderRequestDto;
	}

	private OrderRequestDto mapToOrders(Orders orderRequest) {
		OrderRequestDto orderRequestDto = new OrderRequestDto();
		orderRequestDto.setOrderId(orderRequest.getId());
		orderRequestDto.setUserId(orderRequest.getUserId());
		orderRequestDto.setOrderDate(orderRequest.getOrderDate());
		orderRequestDto.setOrderStatus(orderRequest.getOrderStatus());
		orderRequestDto.setTotalBill(orderRequest.getTotalBill());
		return orderRequestDto;
	}

	private ProductDto mapToOrderLineItems(OrderLineItems orderRequest) {
		ProductDto orderLineItems = new ProductDto();
		orderLineItems.setProductId(orderRequest.getProductId());
		orderLineItems.setQuantity(orderRequest.getQuantity());
		orderLineItems.setPrice(orderRequest.getProductPrice());
		orderLineItems.setAmount(orderRequest.getAmount());
		return orderLineItems;
	}

	private OrderLineItems mapToOrderLineProducts(ProductDto orderRequest) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setProductId(orderRequest.getProductId());
		orderLineItems.setQuantity(orderRequest.getQuantity());
		orderLineItems.setProductPrice(orderRequest.getPrice());
		orderLineItems.setAmount(orderRequest.getAmount());
		return orderLineItems;
	}

	public OrderRequestDto getOrder(Long orderId) {
		Orders order = ordersRepository.findById(orderId).get();

		OrderRequestDto orderRequestDto = mapToOrders(order);

		List<ProductDto> orderLineItems = order.getOrderLineItemsList().stream().map(this::mapToOrderLineItems)
				.toList();

		for (ProductDto product : orderLineItems) {
//			ProductDto productDto = restTemplate.getForObject(
//					"http://product-service/api/product/byId/" + product.getProductId(), ProductDto.class);
			ProductDto productDto=getProductDetailByProductId(product.getProductId());
			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			// mapToOrderLineProducts(user);
			// responseDtos.add(items);
		}
//		UserDto userDto = restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" + order.getUserId(),
//				UserDto.class);
		//UserDto userDto = getUserDetailByUserId(order.getUserId());
		//orderRequestDto.setUserDetail(userDto);
		orderRequestDto.setOrderLineProductList(orderLineItems);
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
	/*
	 * Orders order = ordersRepository.findById(orderId).get();
	 * 
	 * List<ProductDto> responseDtos = new ArrayList<>(); OrderRequestDto
	 * orderRequestDto = mapToOrders(order);
	 * 
	 * List<OrderLineItems> orderLineItems =
	 * orderRequestDto.getOrderLineItemsDtoList().stream()
	 * .map(this::mapToOrderLineProducts).toList();
	 * 
	 * for (OrderLineItems user : orderLineItems) { ProductDto productDto =
	 * restTemplate .getForObject("http://product-service/api/product/byId/" +
	 * user.getProductId(), ProductDto.class);
	 * 
	 * ProductDto items = mapToOrderLineItems(user);
	 * items.setName(productDto.getName());
	 * items.setDescription(productDto.getDescription()); responseDtos.add(items); }
	 * 
	 * UserDto userDto =
	 * restTemplate.getForObject("http://user-service/api/user/get-user-by-id/" +
	 * order.getUserId(), UserDto.class);
	 * 
	 * orderRequestDto.setUserDetail(userDto);
	 * orderRequestDto.setOrderLineItemsDtoList(responseDtos);
	 * 
	 */
}
