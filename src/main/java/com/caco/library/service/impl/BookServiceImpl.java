package com.caco.library.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.caco.library.exception.BookDoesNotExistException;
import com.caco.library.exception.BookNotAvailableException;
import com.caco.library.model.entity.BookEntity;
import com.caco.library.repository.BookRepository;
import com.caco.library.service.BookService;

import jakarta.transaction.Transactional;

@Service
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public BookEntity checkBook(Long bookId) {
		BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(BookDoesNotExistException::new);
		checkBookAvailability(bookEntity);
		return bookEntity;
	}

	@Override
	@Transactional
	public void updateBook(BookEntity bookEntity) {
		bookRepository.save(bookEntity);
	}

	private void checkBookAvailability(BookEntity bookEntity) {
		if (bookEntity.getAvailableCopies() <= 0) {
			throw new BookNotAvailableException();
		}
	}
}
