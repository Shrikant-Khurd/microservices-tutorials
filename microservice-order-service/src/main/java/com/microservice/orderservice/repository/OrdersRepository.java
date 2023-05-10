package com.microservice.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders,Long> {

	List<Orders> findOrdersByUserId(long userId);
}
