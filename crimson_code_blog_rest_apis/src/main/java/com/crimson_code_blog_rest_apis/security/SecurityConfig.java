package com.crimson_code_blog_rest_apis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.crimson_code_blog_rest_apis.utils.JwtUtils;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private UserDetailsService userDetailsService;
	private JwtUtils jwtUtils;
	private HandlerExceptionResolver exceptionResolver;
	
	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, JwtUtils jwtUtils,
			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
		this.userDetailsService = userDetailsService;
		this.jwtUtils = jwtUtils;
		this.exceptionResolver = exceptionResolver;
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorize -> {
					authorize.requestMatchers("/api/auth/logout").authenticated()
					.requestMatchers("/api/auth/**").permitAll()
					
					/*
					 * Make the default error handling endpoint accessible for everyone
					 * to make the error shown in the response body when an error occurs
					 */
					.requestMatchers("/error").permitAll()
					.anyRequest().authenticated();
				})
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				
				/* 
				 * This handles the unauthorized exceptions manually to return 401
				 * because spring by default return 403 when the user is unauthorized
				 * instead of 401 when using custom login functionality like JWT in our case
				 */

				.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, exception) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
				}))
				.build();
	}
	
	private JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtUtils, exceptionResolver, userDetailsService);
	}

	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
		daoAuthProvider.setPasswordEncoder(passwordEncoder());
		daoAuthProvider.setUserDetailsService(userDetailsService);
		
		return daoAuthProvider;
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration AuthConfig) throws Exception {
		return AuthConfig.getAuthenticationManager();
	}
}
