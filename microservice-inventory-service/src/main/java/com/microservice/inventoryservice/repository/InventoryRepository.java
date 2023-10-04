package com.microservice.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	public Inventory findByProductId(long productId);
	public List<Inventory> findProductByProductId(long productId);
}