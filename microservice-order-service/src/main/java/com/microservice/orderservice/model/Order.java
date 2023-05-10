package com.microservice.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long userId;
    private long productId;
    private long quantity;
    private Instant orderDate;
    private String orderStatus;
    
    @Column(name = "product_price")
    private long productPrice;
    @Column(name = "TOTAL_AMOUNT")
    private long amount;
    
    
}
