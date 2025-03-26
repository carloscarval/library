package com.caco.library.service;

import com.caco.library.model.entity.BookEntity;

public interface BookService {

	BookEntity getBookById(Long bookId);

	void updateBook(BookEntity bookEntity);
}
