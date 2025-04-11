package com.usecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usecom.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
}
