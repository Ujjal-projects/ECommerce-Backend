package com.usecom.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usecom.config.JwtProvider;
import com.usecom.domain.USER_ROLE;
import com.usecom.entity.Cart;
import com.usecom.entity.Seller;
import com.usecom.entity.User;
import com.usecom.entity.VarificationCode;
import com.usecom.exception.InvalidOtpException;
import com.usecom.repository.CartRepository;
import com.usecom.repository.SellerRepository;
import com.usecom.repository.UserRepository;
import com.usecom.repository.VarificationCodeRepository;
import com.usecom.request.LoginRequest;
import com.usecom.response.AuthResponse;
import com.usecom.response.SignUpRequest;
import com.usecom.util.OtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VarificationCodeRepository varificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserServiceImpl;
    private final SellerRepository sellerRepository;

    @Override
    public String createUser(SignUpRequest req) {
    	
    	VarificationCode varificationCode = varificationCodeRepository.findByEmail(req.getEmail());
    	if (varificationCode == null || !varificationCode.getOtp().equals(req.getOtp())) {
    		throw new InvalidOtpException("Wrong OTP provided.");
    	}

        User user = userRepository.findByEmail(req.getEmail());

        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("990733035");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = 
            new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

	@Override
	public void sendLoginAndSignupOtp(String email, USER_ROLE role) throws Exception {
		String SINGING_PREFIX = "signing_";
		String SELLER_PREFIX = "seller_";
		
		if(email.startsWith(SINGING_PREFIX)) {
			email = email.substring(SINGING_PREFIX.length());
			if(role.equals(USER_ROLE.ROLE_SELLER)) {
				Seller seller = sellerRepository.findByEmail(email);
				if(seller == null) {
					throw new Exception("seller not found with email - "+email);
				}
			}
			else {
				User  user = userRepository.findByEmail(email);
				if(user==null) {
					throw new Exception("user not exist with provided  email");
			}

		}
		VarificationCode isExist = varificationCodeRepository.findByEmail(email);
		if(isExist != null) {
			varificationCodeRepository.delete(isExist);
		}
		String otp = OtpUtil.generateOtp();
		
		VarificationCode varificationCode = new VarificationCode();
		varificationCode.setOtp(otp);
		varificationCode.setEmail(email);
		varificationCodeRepository.save(varificationCode);
		
		String subject = "MS Enterprice login/signup otp";
		String text = "your login/signup otp is - " + otp;
		
		emailService.sendVarificationOtpEmail(email, otp, subject, text);
		}
		
	}

	@Override
	public AuthResponse singing(LoginRequest req) {
		String username = req.getEmail();
		String otp = req.getOtp();
		
		Authentication authentication = authenticate(username,otp);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Login success");
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
		
		authResponse.setRole(USER_ROLE.valueOf(roleName));
		
		return authResponse;
	}

	private Authentication authenticate(String username, String otp) {
		UserDetails userDetails= customUserServiceImpl.loadUserByUsername(username);
		if(userDetails == null) {
			throw new BadCredentialsException("invalid username");
		}
		
		VarificationCode varificationCode = varificationCodeRepository.findByEmail(username);
		
		if(varificationCode == null || !varificationCode.getOtp().equals(otp)) {
			throw new BadCredentialsException("wrong otp");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
	}
}
