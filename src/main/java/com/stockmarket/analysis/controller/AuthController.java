package com.stockmarket.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockmarket.analysis.dto.AuthResponse;
import com.stockmarket.analysis.dto.LoginRequest;
import com.stockmarket.analysis.dto.RegisterRequest;
import com.stockmarket.analysis.dto.ResponseStructure;
import com.stockmarket.analysis.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<ResponseStructure<AuthResponse>> register(
			@Valid @RequestBody RegisterRequest request) {

		return authService.register(request);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> login(
			@Valid @RequestBody LoginRequest request) {

		return authService.login(request);
	}
}