package com.usecom.service;

import org.springframework.stereotype.Service;

import com.usecom.config.JwtProvider;
import com.usecom.entity.User;
import com.usecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;
	
	
	@Override
	public User findUserByJwtToken(String jwt) throws Exception {
		String email = jwtProvider.getEmailFromJwtToken(jwt);
		
		return this.findUserByEmail(email);
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		if(user == null) {
			throw new Exception("user not found with email -"+email);
		}
		return user;
	}

}
