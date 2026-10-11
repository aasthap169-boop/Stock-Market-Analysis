package com.stockmarket.analysis.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockmarket.analysis.dao.PortfolioDao;
import com.stockmarket.analysis.dao.UserDao;
import com.stockmarket.analysis.dto.AuthResponse;
import com.stockmarket.analysis.dto.LoginRequest;
import com.stockmarket.analysis.dto.RegisterRequest;
import com.stockmarket.analysis.dto.ResponseStructure;
import com.stockmarket.analysis.dto.UserResponse;
import com.stockmarket.analysis.entity.Portfolio;
import com.stockmarket.analysis.entity.User;
import com.stockmarket.analysis.enums.Role;
import com.stockmarket.analysis.exception.BadRequestException;
import com.stockmarket.analysis.security.CustomUserDetailsService;
import com.stockmarket.analysis.security.JwtService;

@Service
public class AuthService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PortfolioDao portfolioDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Transactional
	public ResponseEntity<ResponseStructure<AuthResponse>> register(
			RegisterRequest request) {

		ResponseStructure<AuthResponse> response =
				new ResponseStructure<>();

		String email = request.getEmail()
				.trim()
				.toLowerCase();

		
		if (userDao.isEmailExists(email)) {

			throw new BadRequestException(
					"Email is already registered"
			);
		}

		User user = new User();

		user.setFirstName(
				request.getFirstName().trim()
		);

		user.setLastName(
				request.getLastName().trim()
		);

		user.setEmail(email);

		user.setPassword(
				passwordEncoder.encode(
						request.getPassword()
				)
		);

		user.setRole(Role.USER);

		User savedUser = userDao.saveUser(user);

		Portfolio portfolio = new Portfolio();

		portfolio.setUser(savedUser);

		portfolioDao.savePortfolio(portfolio);

		UserDetails userDetails =customUserDetailsService.loadUserByUsername(email);

		String token =jwtService.generateToken(userDetails);

		AuthResponse authResponse =createAuthResponse(savedUser,token);

		response.setStatusCode(HttpStatus.CREATED.value());

		response.setMessage(
				"Registration successful"
		);

		response.setData(authResponse);

		return new ResponseEntity<>(
				response,
				HttpStatus.CREATED
		);
	}

	public ResponseEntity<ResponseStructure<AuthResponse>> login(
			LoginRequest request) {

		ResponseStructure<AuthResponse> response =
				new ResponseStructure<>();

		String email = request.getEmail()
				.trim()
				.toLowerCase();

		// Authenticate user
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						email,
						request.getPassword()
				)
		);

		// Get user
		Optional<User> optionalUser =
				userDao.getUserByEmail(email);

		if (optionalUser.isEmpty()) {

			throw new BadRequestException(
					"Invalid email or password"
			);
		}

		User user = optionalUser.get();

		// Get user details
		UserDetails userDetails =
				customUserDetailsService
						.loadUserByUsername(email);

		// Generate JWT
		String token =
				jwtService.generateToken(userDetails);

		// Create response
		AuthResponse authResponse =
				createAuthResponse(
						user,
						token
				);

		response.setStatusCode(
				HttpStatus.OK.value()
		);

		response.setMessage(
				"Login successful"
		);

		response.setData(authResponse);

		return new ResponseEntity<>(
				response,
				HttpStatus.OK
		);
	}

	private AuthResponse createAuthResponse(
			User user,
			String token) {

		UserResponse userResponse =
				new UserResponse();

		userResponse.setId(user.getId());

		userResponse.setFirstName(
				user.getFirstName()
		);

		userResponse.setLastName(
				user.getLastName()
		);

		userResponse.setEmail(
				user.getEmail()
		);

		userResponse.setRole(
				user.getRole()
		);

		AuthResponse authResponse =
				new AuthResponse();

		authResponse.setToken(token);

		authResponse.setTokenType(
				"Bearer"
		);

		authResponse.setUser(
				userResponse
		);

		return authResponse;
	}
}