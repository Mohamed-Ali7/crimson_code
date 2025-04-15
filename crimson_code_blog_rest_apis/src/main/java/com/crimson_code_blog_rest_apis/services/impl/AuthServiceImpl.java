package com.crimson_code_blog_rest_apis.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.security.UserPrincipal;
import com.crimson_code_blog_rest_apis.dto.request.EmailVerificationRequest;
import com.crimson_code_blog_rest_apis.dto.request.LoginRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.LoginResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.RefreshTokenResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;
import com.crimson_code_blog_rest_apis.entity.RoleEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exceptions.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.exceptions.JwtTokenException;
import com.crimson_code_blog_rest_apis.exceptions.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.RoleRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.services.AuthService;
import com.crimson_code_blog_rest_apis.services.EmailService;
import com.crimson_code_blog_rest_apis.utils.JwtTokenType;
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
		
		Map<String, Object> userClaims = generateUserClaims(userPrincipal.getUserEntity());
		
		String accessToken = jwtUtils.generateAccessToken(loginRequest.getEmail(), userClaims);
		String refreshToken = jwtUtils.generateRefreshToken(loginRequest.getEmail());
		
		return new LoginResponseModel(accessToken, refreshToken);
	}


	@Override
	public void emailVerification(String token) {
		
		jwtUtils.validateJwtToken(token, JwtTokenType.EMAIL_VERIFICATION_TOKEN.getValue());
		
		UserEntity user = userRepository.findByEmailVerificationToken(token)
				.orElseThrow(() -> new JwtTokenException(JwtTokenType.EMAIL_VERIFICATION_TOKEN.getValue(),
						"Invalid Email verification token "));
		
		user.setEmailVerificationToken(null);
		user.setIsEmailVerified(true);
		
		userRepository.save(user);
		
	}

	@Override
	public void emailVerificationRequest(EmailVerificationRequest verificationRequest) {
		String userEmail = verificationRequest.getEmail();

		UserEntity user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with email: " + userEmail));
		
		if (user.getIsEmailVerified()) {
			throw new CrimsonCodeGlobalException("Your email has been already verified");
		}
		
		String emailVerificationToken = jwtUtils.generateEmailVerificationToken(userEmail);
		user.setEmailVerificationToken(emailVerificationToken);
		
		// Send a new email verification token to user's email address
		emailService.sendVerificationEmail(userEmail, emailVerificationToken);

		userRepository.save(user);
	}


	@Override
	public RefreshTokenResponseModel refreshAccessToken(String authorizationHeader) {

		if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new JwtTokenException(JwtTokenType.REFRESH_TOKEN.getValue(), "Invalid Refresh token");
		}
		
		String refreshToken = authorizationHeader.substring(7);
		
		jwtUtils.validateJwtToken(refreshToken, JwtTokenType.REFRESH_TOKEN.getValue());
		
		String userEmail = jwtUtils.extractUsername(refreshToken);
		
		UserEntity user = userRepository.findByEmail(userEmail)
				.orElseThrow(() ->
				new JwtTokenException(JwtTokenType.REFRESH_TOKEN.getValue(), "Invalid Refresh token"));
		
		Map<String, Object> userClaims = generateUserClaims(user);
		
		String newAccessToken = jwtUtils.generateAccessToken(userEmail, userClaims);
		 
		return new RefreshTokenResponseModel(newAccessToken);
	}
	
	private Map<String, Object> generateUserClaims(UserEntity user) {

		Map<String, Object> userClaims = new HashMap<>();
		
		List<String> userRoles = user.getRoles().stream()
				.map(role -> role.getName()).collect(Collectors.toList());
		userClaims.put("roles", userRoles);
		userClaims.put("userPublicId", user.getPublicId());
		
		return userClaims;
	}
}
