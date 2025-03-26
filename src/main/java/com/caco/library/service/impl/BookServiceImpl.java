package com.caco.library.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.caco.library.exception.BookDoesNotExistException;
import com.caco.library.exception.DataChangedException;
import com.caco.library.model.entity.BookEntity;
import com.caco.library.repository.BookRepository;
import com.caco.library.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Cacheable(value = "books", key = "#bookId")
	@Transactional(readOnly = true)
	@Override
	public BookEntity getBookById(Long bookId) {
		return bookRepository.findById(bookId).orElseThrow(BookDoesNotExistException::new);
	}

	@Override
	@Transactional
	@CacheEvict(value = "books", key = "#bookEntity.id")
	public void updateBook(BookEntity bookEntity) {
		try {
			bookRepository.save(bookEntity);
		} catch (ObjectOptimisticLockingFailureException e) {
			throw new DataChangedException();
		}
	}
}
