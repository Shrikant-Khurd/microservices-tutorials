package com.smartshopper.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartshopper.orderservice.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders,Long> {

	List<Orders> findOrdersByUserId(long userId);



}
