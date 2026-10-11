package com.stockmarket.analysis.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration}")
	private long jwtExpiration;

	public String generateToken(UserDetails userDetails) {

		Date now = new Date();

		Date expiration = new Date(
				now.getTime() + jwtExpiration
		);

		return Jwts.builder()
				.subject(userDetails.getUsername())
				.issuedAt(now)
				.expiration(expiration)
				.signWith(getSigningKey())
				.compact();
	}

	public String extractUsername(String token) {

		return extractClaims(token).getSubject();
	}

	public boolean isTokenValid(
			String token,
			UserDetails userDetails) {

		String username = extractUsername(token);

		return username.equalsIgnoreCase(
				userDetails.getUsername()
		) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {

		return extractClaims(token)
				.getExpiration()
				.before(new Date());
	}

	private Claims extractClaims(String token) {

		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	private SecretKey getSigningKey() {

		byte[] keyBytes =
				Decoders.BASE64.decode(jwtSecret);

		return Keys.hmacShaKeyFor(keyBytes);
	}
}