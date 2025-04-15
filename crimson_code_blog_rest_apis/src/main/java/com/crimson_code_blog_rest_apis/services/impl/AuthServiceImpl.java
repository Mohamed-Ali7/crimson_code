package com.crimson_code_blog_rest_apis.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.management.relation.RoleResult;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.security.UserPrincipal;
import com.crimson_code_blog_rest_apis.dto.request.LoginRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.LoginResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;
import com.crimson_code_blog_rest_apis.entity.RoleEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exceptions.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.repository.RoleRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.services.AuthService;
import com.crimson_code_blog_rest_apis.services.EmailService;
import com.crimson_code_blog_rest_apis.utils.JwtUtils;
import com.crimson_code_blog_rest_apis.utils.UserRoles;

@Service
public class AuthServiceImpl implements AuthService {

	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private RoleRepository roleRepository;
	private JwtUtils jwtUtils;
	private EmailService emailService;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	
	@Autowired
	public AuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository,
			JwtUtils jwtUtils, EmailService emailService, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.roleRepository = roleRepository;
		this.jwtUtils = jwtUtils;
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}


	@Override
	public RegisterResponseModel register(RegisterRequestModel registerRequest) {
		if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
			throw new CrimsonCodeGlobalException("This email already exists");
		}
		
		UserEntity newUser = modelMapper.map(registerRequest, UserEntity.class);
		
		newUser.setPublicId(UUID.randomUUID().toString());
		
		RoleEntity userRole = roleRepository.findByName(UserRoles.ROLE_USER.name())
				.orElseGet(() -> {
					RoleEntity newUserRole = new RoleEntity(UserRoles.ROLE_USER.name());
					return roleRepository.save(newUserRole);
				});
		
		String emailVerificationToken = jwtUtils.generateEmailVerificationToken(newUser.getEmail());
		newUser.setEmailVerificationToken(emailVerificationToken);
		
		newUser.addRole(userRole);
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setJoinedAt(LocalDateTime.now());
		
		UserEntity savedUser = userRepository.save(newUser);
		
		RegisterResponseModel registerResponse = modelMapper.map(savedUser, RegisterResponseModel.class);
		
		emailService.sendVerificationEmail(savedUser.getEmail(), emailVerificationToken);
		
		return registerResponse;
	}


	@Override
	public LoginResponseModel login(LoginRequestModel loginRequest) {

		Authentication auth = 
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
		Authentication authenticatedToken = authenticationManager.authenticate(auth);
		
		SecurityContextHolder.getContext().setAuthentication(authenticatedToken);		
		
		UserPrincipal userPrincipal = (UserPrincipal) authenticatedToken.getPrincipal();
		
		List<String> userRoles = authenticatedToken.getAuthorities().stream()
				.map(authority -> authority.getAuthority()).collect(Collectors.toList());
		Map<String, Object> userClaims = new HashMap<>();
		
		userClaims.put("roles", userRoles);
		userClaims.put("userPublicId", userPrincipal.getPublicId());
		
		String accessToken = jwtUtils.generateAccessToken(loginRequest.getEmail(), userClaims);
		String refreshToken = jwtUtils.generateRefreshToken(loginRequest.getEmail());
		
		return new LoginResponseModel(accessToken, refreshToken);
	}

}
