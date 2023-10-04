package com.smartshopper.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartshopper.inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	public Inventory findByProductId(long productId);
}
