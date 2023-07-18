package com.microservice.orderservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.orderservice.dto.OrderRequestDto;
import com.microservice.orderservice.dto.ResponseDto;
import com.microservice.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/add-order")
	public ResponseEntity<ResponseDto> placeOrders(@RequestBody OrderRequestDto orderRequest) {
		ResponseDto savedOrder = orderService.placeOrders(orderRequest);
		return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
	}

	@GetMapping("order-by-orderId/{id}")
	public ResponseEntity<OrderRequestDto> getOrder(@PathVariable("id") Long orderId) {
		OrderRequestDto responseDto = orderService.getOrder(orderId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@GetMapping("/all-orders")
	public ResponseEntity<List<OrderRequestDto>> getAllOrders() {
		List<OrderRequestDto> allOrderList = orderService.getAllOrders();
		return new ResponseEntity<>(allOrderList, HttpStatus.OK);
	}

	@GetMapping("/get-orderDetail-userId/{id}")
	public ResponseEntity<List<OrderRequestDto>> getAllOrderByUserId(@PathVariable("id") long userId) {
		List<OrderRequestDto> getAllUserOrderList = orderService.getAllOrderByUserId(userId);
		return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
	}

	@GetMapping("/get-order-userId/{id}")
	public ResponseEntity<ResponseDto> getOrderByUserId(@PathVariable("id") long userId) {
		ResponseDto getAllUserOrderList = orderService.getOrderByUserId(userId);
		return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
	}
}
