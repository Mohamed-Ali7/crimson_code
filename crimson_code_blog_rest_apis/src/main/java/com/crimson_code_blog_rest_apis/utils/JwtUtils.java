package com.crimson_code_blog_rest_apis.utils;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtils {

	@Value("${emailVerificationTokenExpirationDate}")
	private long emailVerificationTokenExpirationDate;
	
	@Value("${tokenSecert}")
	private String secretKey;
	
	public String generateEmailVerificationToken(String username) {
		return generateJwtToken(username, null, emailVerificationTokenExpirationDate);
	}
	
	private String generateJwtToken(String subject, Map<String, Object> claims, long expirationDate) {
		JwtBuilder jwt = Jwts.builder();
		
		jwt.subject(subject)
		.expiration(new Date(System.currentTimeMillis() + expirationDate))
		.signWith(key())
		.issuedAt(new Date());
		
		if (claims != null) {
			jwt.claims(claims);
		}
		
		return jwt.compact();
	}
	
	Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
}
