package com.microservice.usercartservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "usercart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String cartNumber;
	private long userId;
	private Instant cartDate;
	private long productId;
	private long quantity;
	private boolean cartSelected;
	
	
	

//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinTable(name="cartLineItemsList")
//	private List<CartLineItems> cartLineItemsList;
}
