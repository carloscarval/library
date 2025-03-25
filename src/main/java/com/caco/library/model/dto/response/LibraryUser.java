package com.caco.library.model.dto.response;

import com.caco.library.model.entity.LibraryUserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LibraryUser(
		Long libraryUserId,
		String username,
		String email
) {
	public static LibraryUser fromEntity(LibraryUserEntity entity) {
		return new LibraryUser(
				entity.getId(),
				entity.getUsername(),
				entity.getEmail()
		);
	}
}