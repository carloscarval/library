package com.caco.library.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.caco.library.model.dto.request.AuthenticationRequest;
import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.security.service.AuthService;
import com.caco.library.security.util.JwtUtil;
import com.caco.library.service.LibraryUserService;

import static com.caco.library.utils.LibraryConstants.API_AUTH;
import static com.caco.library.utils.LibraryConstants.API_BASE_URL;
import static com.caco.library.utils.LibraryConstants.API_LOGIN;
import static com.caco.library.utils.LibraryConstants.API_REGISTER;
import static com.caco.library.utils.LibraryConstants.API_VERSION;

@RestController
@RequestMapping(API_BASE_URL + API_VERSION + API_AUTH)
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping(API_LOGIN)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
		return ResponseEntity.ok(authService.authenticateUser(authenticationRequest));
	}

	@PostMapping(API_REGISTER)
	public ResponseEntity<?> registerUser(@RequestBody CreateUserRequest userRequest) {
		return ResponseEntity.ok(authService.registerUser(userRequest));
	}
}
