package com.usecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usecom.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{
	
}
