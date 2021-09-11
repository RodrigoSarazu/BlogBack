package com.blog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.dto.LoginRequest;
import com.blog.dto.RegisterRequest;
import com.blog.model.User;
import com.blog.repository.UserRepository;
import com.blog.security.JwtProvider;

@Service
public class AuthService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	public void signup(RegisterRequest registerRequest) {
		User user=new User();
		user.setUserName(registerRequest.getUsername());
		//agrega el metodo
		user.setPassword(encodePassword(registerRequest.getPassword()));
		user.setEmail(registerRequest.getEmail());
		userRepository.save(user);
	}
	//Se encripta automaticamente
	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	//ordenando el usuario con token
	@SuppressWarnings("unused")
	public AuthenticationResponse login(LoginRequest loginRequest) {
		
	Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
			loginRequest.getPassword()));
	SecurityContextHolder.getContext().setAuthentication(authenticate);
	String authenticationToken = jwtProvider.generateToken(authenticate);
		return new AuthenticationResponse(authenticationToken,loginRequest.getUsername()) ;
	}
	 public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
	        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
	                getContext().getAuthentication().getPrincipal();
	        return Optional.of(principal);
	    }
}
