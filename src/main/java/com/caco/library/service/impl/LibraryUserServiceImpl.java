package com.caco.library.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.caco.library.exception.UserDoesNotExistException;
import com.caco.library.exception.UsernameAlreadyTakenException;
import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.repository.LibraryUserRepository;
import com.caco.library.service.LibraryUserService;

import jakarta.transaction.Transactional;

import static com.caco.library.utils.LibraryMessages.GENERIC_ERROR;

@Service
public class LibraryUserServiceImpl implements LibraryUserService {

	private final LibraryUserRepository libraryUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public LibraryUserServiceImpl(LibraryUserRepository libraryUserRepository) {
		this.libraryUserRepository = libraryUserRepository;
	}

	@Override
	public LibraryUserEntity findByUsername(String username) {
		return libraryUserRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
	}

	@Override
	public LibraryUserEntity checkLibraryUser(Long libraryUserId) {
		return libraryUserRepository.findById(libraryUserId).orElseThrow(UserDoesNotExistException::new);
	}

	@Override
	@Transactional
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
			throw new RuntimeException(GENERIC_ERROR, e);
		}
		return libraryUserEntity;
	}
}
