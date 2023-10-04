package com.smartshopper.orderservice.controller;

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

import com.smartshopper.orderservice.dto.ApiResponse;
import com.smartshopper.orderservice.dto.OrderRequestDto;
import com.smartshopper.orderservice.dto.ResponseDto;
import com.smartshopper.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add-order/{isSingleProductCheckout}")
    public ResponseEntity<ApiResponse> placeOrders(
            @PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout,
            @RequestBody OrderRequestDto orderRequest) {
        ApiResponse savedOrder = orderService.placeOrders(orderRequest, isSingleProductCheckout);
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
    public ResponseEntity<ApiResponse> getAllOrderByUserId(@PathVariable("id") long userId) {
        ApiResponse getAllUserOrderList = orderService.getAllOrderByUserId(userId);
        return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
    }

    @GetMapping("/get-orders-by-userId")
    public ResponseEntity<ApiResponse> getOrderByUserIdd() {
        ApiResponse getAllUserOrderList = orderService.getOrderByUserId();
        return new ResponseEntity<>(getAllUserOrderList, HttpStatus.OK);
    }
}
