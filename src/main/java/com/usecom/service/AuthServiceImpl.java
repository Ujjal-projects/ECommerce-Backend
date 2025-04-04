package com.usecom.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usecom.config.JwtProvider;
import com.usecom.domain.USER_ROLE;
import com.usecom.entity.Cart;
import com.usecom.entity.User;
import com.usecom.entity.VarificationCode;
import com.usecom.exception.InvalidOtpException;
import com.usecom.repository.CartRepository;
import com.usecom.repository.UserRepository;
import com.usecom.repository.VarificationCodeRepository;
import com.usecom.response.SignUpRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VarificationCodeRepository varificationCodeRepository;

    @Override
    public String createUser(SignUpRequest req) {
    	
    	// Check if OTP exists and matches
    	VarificationCode varificationCode = varificationCodeRepository.findByEmail(req.getEmail());
    	if (varificationCode == null || !varificationCode.getOtp().equals(req.getOtp())) {
    		throw new InvalidOtpException("Wrong OTP provided.");
    	}

        // Check if user exists
        User user = userRepository.findByEmail(req.getEmail());

        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("990733035");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

            user = userRepository.save(createdUser);

            // Create a cart for the new user
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        // Grant authentication
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = 
            new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate and return JWT token
        return jwtProvider.generateToken(authentication);
    }
}
