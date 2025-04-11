package com.usecom.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usecom.config.JwtProvider;
import com.usecom.domain.ACCOUNT_STATUS;
import com.usecom.domain.USER_ROLE;
import com.usecom.entity.Address;
import com.usecom.entity.Seller;
import com.usecom.repository.AddressRepository;
import com.usecom.repository.SellerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService{

	private final SellerRepository sellerRepository;
	private final JwtProvider  jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final AddressRepository addressRepository;
	
	@Override
	public Seller getSellerProfile(String jwt) throws Exception {
		String email = jwtProvider.getEmailFromJwtToken(jwt);
		return this.getSellerByEmail(email);
	}

	@Override
	public Seller creteSeller(Seller seller) throws Exception {
		Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
		if(sellerExist != null) {
			throw new Exception("seller alredy exist, used diffarent email");
		}
		Address savedAddress = addressRepository.save(seller.getPickupAddress());
		
		Seller newSeller = new Seller();
		newSeller.setEmail(seller.getEmail());
		newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
		newSeller.setSellerName(seller.getSellerName());
		newSeller.setPickupAddress(savedAddress);
		newSeller.setGSTIN(seller.getGSTIN());
		newSeller.setRole(USER_ROLE.ROLE_SELLER);
		newSeller.setMobile(seller.getMobile());
		newSeller.setBankDetails(seller.getBankDetails());
		newSeller.setBusinessDetails(seller.getBusinessDetails());

		return sellerRepository.save(newSeller);
	}

	@Override
	public Seller getSellerById(long id) throws Exception{
		return sellerRepository.findById(id).orElseThrow(()-> new Exception("seller"
				+ "not found with id- "+id));
	}

	@Override
	public Seller getSellerByEmail(String email) throws Exception {
		Seller seller = sellerRepository.findByEmail(email);
		if(seller == null) {
			throw new Exception("seller not found");
		}
		return seller;
	}

	@Override
	public List<Seller> getAllSellers(ACCOUNT_STATUS status) {
		return sellerRepository.findByAccountStatus(status);
	}

	@Override
	public Seller updateSeller(Long id, Seller seller) throws Exception {
		Seller existingSeller = sellerRepository.findById(id)
				.orElseThrow(()->
						new Exception("Seller not found with id -" +id));
		
		if(seller.getSellerName() != null) {
			existingSeller.setSellerName(seller.getSellerName());
		}
		if(seller.getMobile() != null) {
			existingSeller.setMobile(seller.getMobile());
		}
		if(seller.getEmail() != null) {
			existingSeller.setEmail(seller.getEmail());
		}
		if(seller.getBusinessDetails() != null &&
				seller.getBusinessDetails().getBusinessName() != null) {
			existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
		}
		if(seller.getBankDetails() != null
				&& seller.getBankDetails().getAccountHolderName() != null
				&& seller.getBankDetails().getAccountNumber() != null
				&& seller.getBankDetails().getBankName() != null
				&& seller.getBankDetails().getIfscCode() != null
				) {
			
			existingSeller.getBankDetails().setAccountHolderName(
					seller.getBankDetails().getAccountHolderName()
					);
			
			existingSeller.getBankDetails().setAccountNumber(
			seller.getBankDetails().getAccountNumber()
			);
			
			existingSeller.getBankDetails().setBankName(
			seller.getBankDetails().getBankName()
			);
			
			existingSeller.getBankDetails().setIfscCode(
					seller.getBankDetails().getIfscCode());
		}
		if(seller.getPickupAddress() != null
				&& seller.getPickupAddress().getAddress() != null
				&& seller.getPickupAddress().getMobile() != null
				&& seller.getPickupAddress().getCity() != null
				&& seller.getPickupAddress().getState() !=null
				) {
			existingSeller.getPickupAddress()
			.setAddress(seller.getPickupAddress().getAddress());
			existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
			existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
			existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
			existingSeller.getPickupAddress().setPincode(seller.getPickupAddress().getPincode());
		}
		if(seller.getGSTIN() != null) { 
			existingSeller.setGSTIN(seller.getGSTIN());
		}
		
		return sellerRepository.save(existingSeller);
	}

	@Override
	public void deleteSeller(Long id) throws Exception {
		Seller seller = getSellerById(id);
		sellerRepository.delete(seller);
	}

	@Override
	public Seller varifyEmail(String email, String otp) throws Exception {
		Seller seller = getSellerByEmail(email);
		seller.setEmailVarified(true);
		return sellerRepository.save(seller);
	}

	@Override
	public Seller updateSellerAccountStatus(Long sellerId, ACCOUNT_STATUS status) throws Exception {
		Seller seller = getSellerById(sellerId);
		seller.setAccountStatus(status);
		return sellerRepository.save(seller);
	}

}
