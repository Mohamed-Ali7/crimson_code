package com.crimson_code_blog_rest_apis;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.repository.TokenBlacklistRepository;

import jakarta.transaction.Transactional;

@Service
@org.springframework.scheduling.annotation.EnableScheduling
public class EnableScheduling {

	private TokenBlacklistRepository tokenBlacklistRepository;

	@Autowired
	public EnableScheduling(TokenBlacklistRepository tokenBlacklistRepository) {
		this.tokenBlacklistRepository = tokenBlacklistRepository;
	}



	@Scheduled(cron = "0 * * * * *" ) //every hour
	@Transactional
	void revokeExpiredTokenFronBlacklist() {
		tokenBlacklistRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
	}
}
