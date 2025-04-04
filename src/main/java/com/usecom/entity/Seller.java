package com.usecom.entity;

import com.usecom.domain.ACCOUNT_STATUS;
import com.usecom.domain.USER_ROLE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
public class Seller {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String sellerName;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	private String password;
	
	private String mobile;
	
	@Embedded
	private BusinessDetails businessDetails = new BusinessDetails();
	
	@Embedded
	private BankDetails bankDetails = new BankDetails();
	
	@OneToOne(cascade = CascadeType.ALL)
	private Address pickupAddress = new Address();
	
	private String GSTIN;
	
	private USER_ROLE role = USER_ROLE.ROLE_SELLER;
	
	private boolean isEmailVarified = false;
	
	private ACCOUNT_STATUS accountStatus = ACCOUNT_STATUS.PENDING_VERIFICATION;
	
	
}
