package com.crimson_code_blog_rest_apis.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.crimson_code_blog_rest_apis.utils.JwtTokenType;
import com.crimson_code_blog_rest_apis.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtUtils jwtUtils;
	private HandlerExceptionResolver exceptionResolver;
	private UserDetailsService userDetailsService;
	private Map<HttpMethod, String> skipFilterUrls;
	
	public JwtAuthenticationFilter(JwtUtils jwtUtils, HandlerExceptionResolver exceptionResolver,
			UserDetailsService userDetailsService) {
		this.jwtUtils = jwtUtils;
		this.exceptionResolver = exceptionResolver;
		this.userDetailsService = userDetailsService;
		
		skipFilterUrls = new HashMap<>();
		
		skipFilterUrls.put(HttpMethod.GET, "/api/auth/**");
		skipFilterUrls.put(HttpMethod.POST, "/api/auth/**");
	}
	
	

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String url = "";
		String httpMethod = "";

		if (request.getServletPath().equals("/api/auth/logout")) {
			return false;
		}
		
		for (Map.Entry<HttpMethod, String> entry : skipFilterUrls.entrySet()) {
			
			httpMethod = entry.getKey().name();
			url = entry.getValue();
			
			if (new AntPathRequestMatcher(url, httpMethod).matches(request)) {
				return true;
			}
			
			
		}
		
		return false;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			doFilter(request, response, filterChain);
			return;
		}
		
		String token = authHeader.substring(7);
		
		try {
			jwtUtils.validateJwtToken(token, JwtTokenType.ACCESS_TOKEN);
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
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && 
				!jwtUtils.tokenIsBlacklisted(token)) {

			UserPrincipal user = (UserPrincipal) userDetailsService.loadUserByUsername(username);
			
			Authentication authentication = 
					new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		doFilter(request, response, filterChain);

	}

}
