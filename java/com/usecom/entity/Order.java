package com.usecom.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.usecom.domain.ORDER_STATUS;
import com.usecom.domain.PAYMENT_STATUS;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String orderId;
	
	@ManyToOne
	private User user;
	
	private Long sellerId;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

	@ManyToOne
	private Address shippingAddress;

	@Embedded
	private PaymentDetails paymentDetails = new PaymentDetails();
	
	private double totalMrpPrice;
	
	private Integer discount;
	
	private Integer totalSellingPrice;
	
	private ORDER_STATUS orderStatus;
	
	private int totalItems;
	
	private PAYMENT_STATUS paymentStatus = PAYMENT_STATUS.PENDING;
	
	private LocalDateTime orderDate = LocalDateTime.now();
	
	private LocalDateTime deliverDate = orderDate.plusDays(7);
	
	
}
