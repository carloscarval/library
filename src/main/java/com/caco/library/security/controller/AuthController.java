package com.caco.library.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.caco.library.exception.InvalidCredentialsException;
import com.caco.library.model.dto.request.AuthenticationRequest;
import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.model.dto.response.Authentication;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.security.util.JwtUtil;
import com.caco.library.service.LibraryUserService;

import static com.caco.library.utils.LibraryConstants.API_AUTH;
import static com.caco.library.utils.LibraryConstants.API_LOGIN;
import static com.caco.library.utils.LibraryConstants.API_REGISTER;
import static com.caco.library.utils.LibraryMessages.JWT_GENERATED_CORRECTLY;
import static com.caco.library.utils.LibraryMessages.USER_CREATED;

@RestController
@RequestMapping(API_AUTH)
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private LibraryUserService libraryUserService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping(API_LOGIN)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
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
		return ResponseEntity.ok(new Authentication(libraryUserEntity.getId(), jwt, JWT_GENERATED_CORRECTLY));
	}

	@PostMapping(API_REGISTER)
	public ResponseEntity<?> registerUser(@RequestBody CreateUserRequest userRequest) {
		LibraryUserEntity libraryUserEntity = libraryUserService.registerLibraryUser(userRequest);
		return ResponseEntity.ok(new Authentication(libraryUserEntity.getId(), null, USER_CREATED));
	}
}
