package com.caco.library.security.service;

import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.repository.LibraryUserRepository;

import static com.caco.library.utils.LibraryMessages.USER_DOES_NOT_EXIST;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final LibraryUserRepository libraryUserRepository;

	public CustomUserDetailsService(LibraryUserRepository libraryUserRepository) {
		this.libraryUserRepository = libraryUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LibraryUserEntity user = libraryUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_DOES_NOT_EXIST, username)));
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				new ArrayList<>()
		);
	}
}
