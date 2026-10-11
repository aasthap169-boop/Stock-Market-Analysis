package com.stockmarket.analysis.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter
		extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {

		String authorizationHeader =
				request.getHeader("Authorization");

		// Check whether Authorization header exists
		if (authorizationHeader != null
				&& authorizationHeader.startsWith("Bearer ")) {

			String token =
					authorizationHeader.substring(7);

			try {

				String email =
						jwtService.extractUsername(token);

				// Check whether user is not already authenticated
				if (email != null
						&& SecurityContextHolder
								.getContext()
								.getAuthentication()
								== null) {

					UserDetails userDetails =
							customUserDetailsService
									.loadUserByUsername(email);

					// Validate token
					if (jwtService.isTokenValid(
							token,
							userDetails)) {

						UsernamePasswordAuthenticationToken
								authentication =
								new UsernamePasswordAuthenticationToken(
										userDetails,
										null,
										userDetails.getAuthorities()
								);

						SecurityContextHolder
								.getContext()
								.setAuthentication(
										authentication
								);
					}
				}

			} catch (Exception exception) {

			
			}
		}

		filterChain.doFilter(
				request,
				response
		);
	}
}