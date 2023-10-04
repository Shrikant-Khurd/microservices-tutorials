package com.smartshopper.orderservice.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

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
