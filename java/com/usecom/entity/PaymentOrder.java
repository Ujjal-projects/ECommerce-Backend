package com.usecom.entity;

import java.util.HashSet;
import java.util.Set;

import com.usecom.domain.PAYMENT_METHOD;
import com.usecom.domain.PAYMENT_ORDER_STATUS;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PaymentOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long amount;
	
	private PAYMENT_ORDER_STATUS status = PAYMENT_ORDER_STATUS.PENDING;
	
	private PAYMENT_METHOD paymentMethod;
	
	private String paymentLinkId;
	
	@ManyToOne
	private User user;
	
	@OneToMany
	private Set<Order> orders = new HashSet<>();
	
	
}
