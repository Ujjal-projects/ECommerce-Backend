package com.usecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usecom.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long>{

	Seller findByEmail(String email);
}
