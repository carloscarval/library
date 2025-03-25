package com.caco.library.model.dto.response;

import com.caco.library.model.entity.BookEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Book(
		Long bookId,
		String title,
		String author,
		String isbn
) {
	public static Book fromEntity(BookEntity entity) {
		return new Book(
				entity.getId(),
				entity.getTitle(),
				entity.getAuthor(),
				entity.getIsbn()
		);
	}
}