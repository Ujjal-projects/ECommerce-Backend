package com.usecom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usecom.config.JwtProvider;
import com.usecom.domain.ACCOUNT_STATUS;
import com.usecom.entity.Seller;
import com.usecom.entity.SellerReport;
import com.usecom.entity.VarificationCode;
import com.usecom.repository.VarificationCodeRepository;
import com.usecom.request.LoginRequest;
import com.usecom.response.AuthResponse;
import com.usecom.service.AuthService;
import com.usecom.service.EmailService;
import com.usecom.service.SellerService;
import com.usecom.util.OtpUtil;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
	private final SellerService sellerService;
	private final VarificationCodeRepository varificationCodeRepository;
	private final AuthService authService;
	private final EmailService emailService;
	private JwtProvider jwtProvider;
	 

	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginSeller(
			@RequestBody LoginRequest req
			) throws Exception{
		String otp = req.getOtp();
		String email = req.getEmail();

		
		req.setEmail("seller_" + email);
		AuthResponse authResponse = authService.singing(req);
		return ResponseEntity.ok(authResponse);
	}
	
	@PatchMapping("/verify/{otp}")
	public ResponseEntity<Seller> varifySellerEmail(
			@PathVariable String otp) throws Exception{
		VarificationCode varificationCode = varificationCodeRepository.findByOtp(otp);
		if(varificationCode == null || !varificationCode.getOtp().equals(otp)) {
			throw new Exception("wrong otp...!");
		}
		Seller seller = sellerService.varifyEmail(varificationCode.getEmail(), otp);
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Seller> createSeller(
			@RequestBody Seller seller) throws Exception, MessagingException{
		
		Seller savedSeller = sellerService.creteSeller(seller);
		String otp = OtpUtil.generateOtp();
		
		VarificationCode varificationCode = new VarificationCode();
		varificationCode.setOtp(otp);
		varificationCode.setEmail(seller.getEmail());
		varificationCodeRepository.save(varificationCode);
		
		String subject = "MS ENTERPRICE Email Varification Code";
		String text = "Welcome to MS ENTERPRICE, varify your account using this link";
		String frontend_url = "http://localhost:5002/varify-seller/";
		
		emailService.sendVarificationOtpEmail(seller.getEmail(),
				varificationCode.getOtp(), subject, text + frontend_url);
		
		return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception{
		Seller seller = sellerService.getSellerById(id);
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<Seller> getSellerByJwt(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		Seller seller = sellerService.getSellerProfile(jwt);
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}
	
//	@GetMapping("/report")
//	public ResponseEntity<SellerReport> getSellerReport(
//			@RequestHeader() String jwt) throws Exception{
//		String email = jwtProvider.getEmailFromJwtToken(jwt);
//		Seller seller = sellerService.getSellerByEmail(email);
//		SellerReport report = sellerReportService.getSellerReport(seller);
//	}
	
	@GetMapping
	public  ResponseEntity<List<Seller>> getAllSellers(
			@RequestParam(required = false) ACCOUNT_STATUS status){
		List<Seller> sellers = sellerService.getAllSellers(status);
		return ResponseEntity.ok(sellers);
	}
	
	@PatchMapping()
	public ResponseEntity<Seller> updateSeller(
			@RequestHeader("Authorization") String jwt,
			@RequestBody Seller seller) throws Exception{
		Seller profile = sellerService.getSellerProfile(jwt);
		Seller updateSeller = sellerService.updateSeller(profile.getId(), seller);
		return ResponseEntity.ok(updateSeller);
	}
	
	@DeleteMapping("/id")
	public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception{
		sellerService.deleteSeller(id);
		return ResponseEntity.noContent().build();
	}	
	
}
