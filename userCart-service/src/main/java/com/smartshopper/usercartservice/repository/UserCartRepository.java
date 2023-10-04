package com.smartshopper.usercartservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartshopper.usercartservice.model.UserCart;

public interface UserCartRepository extends JpaRepository<UserCart, Long> {

	List<UserCart> findCartByUserId(long userId);

}
