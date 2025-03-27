package com.caco.library.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.caco.library.exception.InvalidCredentialsException;
import com.caco.library.model.dto.request.AuthenticationRequest;
import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.model.dto.response.Authentication;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.security.util.JwtUtil;
import com.caco.library.service.LibraryUserService;

import static com.caco.library.utils.LibraryMessages.JWT_GENERATED_CORRECTLY;
import static com.caco.library.utils.LibraryMessages.USER_CREATED;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;

	private final UserDetailsService userDetailsService;

	private final LibraryUserService libraryUserService;

	private final JwtUtil jwtUtil;

	public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, LibraryUserService libraryUserService, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.libraryUserService = libraryUserService;
		this.jwtUtil = jwtUtil;
	}

	public Authentication authenticateUser(AuthenticationRequest authenticationRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException();
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails.getUsername());
		LibraryUserEntity libraryUserEntity = libraryUserService.findByUsername(authenticationRequest.getUsername());

		return new Authentication(libraryUserEntity.getId(), jwt, JWT_GENERATED_CORRECTLY);
	}

	public Authentication registerUser(CreateUserRequest userRequest) {
		LibraryUserEntity libraryUserEntity = libraryUserService.registerLibraryUser(userRequest);
		return new Authentication(libraryUserEntity.getId(), null, USER_CREATED);
	}
}
