package com.usecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usecom.entity.Seller;
import com.usecom.domain.ACCOUNT_STATUS;


public interface SellerRepository extends JpaRepository<Seller, Long>{

	Seller findByEmail(String email);
	List<Seller> findByAccountStatus(ACCOUNT_STATUS accountStatus);
}
