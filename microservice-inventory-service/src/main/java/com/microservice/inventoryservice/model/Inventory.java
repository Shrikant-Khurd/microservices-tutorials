package com.microservice.inventoryservice.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.microservice.inventoryservice.dto.ProductDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
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
