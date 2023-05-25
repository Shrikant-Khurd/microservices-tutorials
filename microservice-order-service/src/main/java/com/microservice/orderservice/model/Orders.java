package com.microservice.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;

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
	private Instant orderDate;
	private String orderStatus;
	private long totalBill;
	private long userAddressId;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="orderLineItemsList")
	private List<OrderLineItems> orderLineItemsList;
}
