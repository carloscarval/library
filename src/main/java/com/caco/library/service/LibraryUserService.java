package com.caco.library.service;

import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.model.entity.LibraryUserEntity;

public interface LibraryUserService {

	LibraryUserEntity findByUsername(String username);

	LibraryUserEntity checkLibraryUser(Long libraryUserId);

	LibraryUserEntity registerLibraryUser(CreateUserRequest userRequest);
}
