package com.crimson_code_blog_rest_apis.security;

import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.crimson_code_blog_rest_apis.utils.JwtTokenType;
import com.crimson_code_blog_rest_apis.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtUtils jwtUtils;
	private HandlerExceptionResolver exceptionResolver;
	private UserDetailsService userDetailsService;
	
	@Autowired
	public JwtAuthenticationFilter(JwtUtils jwtUtils,
			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver,
			UserDetailsService userDetailsService) {
		this.jwtUtils = jwtUtils;
		this.exceptionResolver = exceptionResolver;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if(request.getServletPath().equals("/api/auth/refresh")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			doFilter(request, response, filterChain);
			return;
		}
		
		String token = authHeader.substring(7);
		
		try {
			jwtUtils.validateJwtToken(token, JwtTokenType.ACCESS_TOKEN.getValue());
		} catch (Exception ex) {
			
			/*
			 * Redirect the exception to be handled by the Custom Exception handler
			 * which in our case GlobalExceptionHandler.class
			 */
			exceptionResolver.resolveException(request, response, null, ex);
			
			// return to prevent the code from going beyond this point
			return;
		}
		
		String username = jwtUtils.extractUsername(token);
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserPrincipal user = (UserPrincipal) userDetailsService.loadUserByUsername(username);
			
			Authentication authentication = 
					new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		doFilter(request, response, filterChain);

	}

}
