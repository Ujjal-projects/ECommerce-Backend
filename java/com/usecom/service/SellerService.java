package com.usecom.service;

import java.util.List;
import java.util.Optional;

import com.usecom.domain.ACCOUNT_STATUS;
import com.usecom.entity.Seller;

public interface SellerService {
	Seller getSellerProfile(String jwt) throws Exception;
	Seller creteSeller(Seller seller) throws Exception;
	Seller getSellerById(long id) throws Exception;
	Seller getSellerByEmail(String email) throws Exception;
	List<Seller> getAllSellers(ACCOUNT_STATUS status);
	Seller updateSeller(Long id, Seller seller) throws Exception;
	void deleteSeller(Long id) throws Exception;
	Seller varifyEmail(String phone, String otp) throws Exception;
	Seller updateSellerAccountStatus(Long sellerId, ACCOUNT_STATUS status) throws Exception;
}
