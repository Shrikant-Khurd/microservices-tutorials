package com.microservice.orderservice.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

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
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String orderNumber;
	private long userId;
	@CreatedDate
	private LocalDateTime orderDate;
	private LocalDateTime shippingDate;
	private String orderStatus;
	private double totalBill;
	private long shippingAddressId;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="orderLineItemsList")
	private List<OrderLineItems> orderLineItemsList;
}
