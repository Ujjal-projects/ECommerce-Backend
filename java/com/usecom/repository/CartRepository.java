package com.usecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usecom.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}
