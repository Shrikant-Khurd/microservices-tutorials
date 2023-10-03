package com.microservice.usercartservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.usercartservice.model.UserCart;

public interface UserCartRepository extends JpaRepository<UserCart,Long> {



	List<UserCart> findCartByUserId(long userId);
	
}
