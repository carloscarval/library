package com.caco.library.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.caco.library.model.dto.request.AuthenticationRequest;
import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.model.dto.response.Authentication;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.repository.LibraryUserRepository;
import com.caco.library.security.util.JwtUtil;

import static com.caco.library.utils.LibraryConstants.API_AUTH;
import static com.caco.library.utils.LibraryConstants.API_LOGIN;
import static com.caco.library.utils.LibraryConstants.API_REGISTER;
import static com.caco.library.utils.LibraryMessages.USERNAME_ALREADY_TAKEN;
import static com.caco.library.utils.LibraryMessages.USER_CREATED;
import static com.caco.library.utils.LibraryMessages.WRONG_USERNAME_OR_PASSWORD;

@RestController
@RequestMapping(API_AUTH)
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private LibraryUserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping(API_LOGIN)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		} catch (BadCredentialsException e) {
			throw new Exception(WRONG_USERNAME_OR_PASSWORD, e);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails.getUsername());

		return ResponseEntity.ok(new Authentication(jwt));
	}

	@PostMapping(API_REGISTER)
	public ResponseEntity<?> registerUser(@RequestBody CreateUserRequest user) {
		LibraryUserEntity libraryUserEntity = new LibraryUserEntity();
		libraryUserEntity.setUsername(user.getUsername());
		libraryUserEntity.setPassword(passwordEncoder.encode(user.getPassword()));
		libraryUserEntity.setEmail(user.getEmail());
		try {
			userRepository.save(libraryUserEntity);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(USERNAME_ALREADY_TAKEN);
		}
		return ResponseEntity.ok(USER_CREATED);
	}
}
