package com.smartshopper.usercartservice.model;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

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
	@CreatedDate
	private LocalDateTime cartDate;
	private long productId;
	private long quantity;
	private boolean cartSelected;

}
