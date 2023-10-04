package com.smartshopper.inventoryservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//    private String skuCode;
	private long productId;
	private Integer quantityInStock;
	private boolean inventoryStatus;
//	@CreatedDate
//	private LocalDateTime createdDate;
//	
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinTable(name="productList")
//	private List<ProductDto> productList;
}
