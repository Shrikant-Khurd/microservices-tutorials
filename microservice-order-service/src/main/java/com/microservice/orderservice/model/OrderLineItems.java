package com.microservice.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "orderlineitems")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long productId;
    @Column(name = "product_price")
    private long productPrice;
    private long quantity;
    @Column(name = "TOTAL_AMOUNT")
    private long amount;
}
