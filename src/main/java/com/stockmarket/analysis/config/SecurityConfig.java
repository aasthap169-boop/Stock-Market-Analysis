package com.stockmarket.analysis.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.stockmarket.analysis.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private UserDetailsService userDetailsService;

	@Value("${app.frontend.url}")
	private String frontendUrl;

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider provider =
				new DaoAuthenticationProvider(
						userDetailsService
				);

		provider.setPasswordEncoder(
				passwordEncoder()
		);

		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration configuration)
			throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http)
			throws Exception {

		http
			.csrf(csrf -> csrf.disable())

			.cors(cors ->
					cors.configurationSource(
							corsConfigurationSource()
					)
			)

			.sessionManagement(session ->
					session.sessionCreationPolicy(
							SessionCreationPolicy.STATELESS
					)
			)

			.authorizeHttpRequests(auth ->
					auth

					.requestMatchers(
							"/auth/**",
							"/stocks/**",
							"/market/**"
					)
					.permitAll()

					.anyRequest()
					.authenticated()
			)

			.authenticationProvider(
					authenticationProvider()
			)

			.addFilterBefore(
					jwtAuthenticationFilter,
					UsernamePasswordAuthenticationFilter.class
			);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration =
				new CorsConfiguration();

		configuration.setAllowedOrigins(
				List.of(frontendUrl)
		);

		configuration.setAllowedMethods(
				List.of(
						"GET",
						"POST",
						"PUT",
						"DELETE",
						"OPTIONS"
				)
		);

		configuration.setAllowedHeaders(
				List.of("*")
		);

		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source =
				new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration(
				"/**",
				configuration
		);

		return source;
	}
}