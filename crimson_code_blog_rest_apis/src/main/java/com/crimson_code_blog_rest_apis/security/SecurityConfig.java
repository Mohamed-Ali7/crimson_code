package com.crimson_code_blog_rest_apis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private UserDetailsService userDetailsService;
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.userDetailsService = userDetailsService;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
					authorize.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
					
					/*
					 * Make the default error handling endpoint accessible for everyone
					 * to make the error shown in the response body when an error occurs
					 */
					.requestMatchers("error").permitAll()
					.anyRequest().authenticated();
				})
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				
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
