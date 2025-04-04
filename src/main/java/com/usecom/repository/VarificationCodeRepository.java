package com.usecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usecom.entity.VarificationCode;

public interface VarificationCodeRepository  extends JpaRepository<VarificationCode, Long>{

	VarificationCode findByEmail(String email);
}
