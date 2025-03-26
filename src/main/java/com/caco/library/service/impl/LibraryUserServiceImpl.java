package com.caco.library.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.caco.library.exception.UserDoesNotExistException;
import com.caco.library.exception.UsernameAlreadyTakenException;
import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.repository.LibraryUserRepository;
import com.caco.library.service.LibraryUserService;

import static com.caco.library.utils.LibraryMessages.UNEXPECTED_EXCEPTION_WHILE_REGISTERING_USER;

@Service
public class LibraryUserServiceImpl implements LibraryUserService {

	private final LibraryUserRepository libraryUserRepository;

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public LibraryUserServiceImpl(LibraryUserRepository libraryUserRepository, PasswordEncoder passwordEncoder) {
		this.libraryUserRepository = libraryUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "users", key = "#username")
	public LibraryUserEntity findByUsername(String username) {
		return libraryUserRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "users", key = "#libraryUserId")
	public LibraryUserEntity checkLibraryUser(Long libraryUserId) {
		return libraryUserRepository.findById(libraryUserId).orElseThrow(UserDoesNotExistException::new);
	}

	@Override
	@Transactional
	@CacheEvict(value = "users", key = "#userRequest.username")
	public LibraryUserEntity registerLibraryUser(CreateUserRequest userRequest) {
		LibraryUserEntity libraryUserEntity = new LibraryUserEntity();
		libraryUserEntity.setUsername(userRequest.getUsername());
		libraryUserEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		libraryUserEntity.setEmail(userRequest.getEmail());
		try {
			libraryUserRepository.save(libraryUserEntity);
		} catch (DataIntegrityViolationException e) {
			throw new UsernameAlreadyTakenException();
		} catch (Exception e) {
			throw new IllegalStateException(UNEXPECTED_EXCEPTION_WHILE_REGISTERING_USER, e);
		}
		return libraryUserEntity;
	}
}
