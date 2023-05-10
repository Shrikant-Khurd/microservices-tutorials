package com.microservice.orderservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.dto.OrderRequestDto;
import com.microservice.orderservice.dto.ResponseDto;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	
	@PostMapping("/add-order")
	public ResponseEntity<Order> placeOrder(@RequestBody Order orderRequest) {
		Order savedOrder= orderService.placeOrder(orderRequest);
		return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
	}
	@PostMapping("/add")
	public ResponseEntity<ResponseDto> placeOrders(@RequestBody OrderRequestDto orderRequest) {
		ResponseDto savedOrder= orderService.placeOrders(orderRequest);
		return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
	}
	
	/*
	 * @GetMapping("order-details/{id}") public ResponseEntity<OrderRequestDto>
	 * getOrderDetails(@PathVariable("id") Long orderId) { OrderRequestDto
	 * responseDto = orderService.getOrderDetail(orderId); return new
	 * ResponseEntity<OrderRequestDto>(responseDto, HttpStatus.OK); }
	 */
	@GetMapping("order-by-id/{id}")
	public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable("id") Long orderId) {
		OrderRequestDto responseDto = orderService.getOrderById(orderId);
		return new ResponseEntity<OrderRequestDto>(responseDto, HttpStatus.OK);
	}

	/*
	 * @GetMapping("order-id/{id}") public ResponseEntity<OrderRequest>
	 * getOrderDetailList(@PathVariable("id") Long orderId) { OrderRequest
	 * responseDto = orderService.getOrderDetailList(orderId); return new
	 * ResponseEntity<OrderRequest>(responseDto, HttpStatus.OK); }
	 */
	@GetMapping("order-detail/{id}")
	public ResponseEntity<ResponseDto> getOrderDetail(@PathVariable("id") Long orderId) {
		ResponseDto responseDto = orderService.getOrderDetails(orderId);
		return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
	}
	
	
	@GetMapping("orderid/{id}")
	public ResponseEntity<OrderRequestDto> getOrder(@PathVariable("id") Long orderId) {
		OrderRequestDto responseDto = orderService.getOrder(orderId);
		return new ResponseEntity<OrderRequestDto>(responseDto, HttpStatus.OK);
	}
	@GetMapping("/all-orders")
	public ResponseEntity<List<OrderRequestDto>> getAllOrders() {
		List<OrderRequestDto> allOrderList = orderService.getAllOrders();
		return new ResponseEntity<List<OrderRequestDto>>(allOrderList, HttpStatus.OK);
	}
	@GetMapping("/get-order-by-userId/{id}")
	public ResponseEntity<List<OrderRequestDto>> getAllOrderByUserId(@PathVariable("id") long userId) {
		List<OrderRequestDto> getAllUserOrderList = orderService.getAllOrderByUserId(userId);
		return new ResponseEntity<List<OrderRequestDto>>(getAllUserOrderList, HttpStatus.OK);
	}
	@GetMapping("/get-order-userId/{id}")
	public ResponseEntity<ResponseDto> getOrderByUserId(@PathVariable("id") long userId) {
		ResponseDto getAllUserOrderList = orderService.getOrderByUserId(userId);
		return new ResponseEntity<ResponseDto>(getAllUserOrderList, HttpStatus.OK);
	}
}
