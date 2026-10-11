package com.stockmarket.analysis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stockmarket.analysis.dao.UserDao;
import com.stockmarket.analysis.entity.User;

@Service
public class CustomUserDetailsService
		implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(
			String email)
			throws UsernameNotFoundException {

		User user = userDao
				.getUserByEmail(email)
				.orElseThrow(() ->
						new UsernameNotFoundException(
								"User not found"
						)
				);

		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.roles(user.getRole().name())
				.build();
	}
}