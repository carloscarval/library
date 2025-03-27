package com.caco.library.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import com.caco.library.exception.BookDoesNotExistException;
import com.caco.library.exception.DataChangedException;
import com.caco.library.model.entity.BookEntity;
import com.caco.library.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookServiceImpl classUnderTest;

	@Nested
	class GetBookByIdTest {

		@Test
		void bookExists_ReturnsBook() {
			BookEntity book = new BookEntity(1L, "title", "author", "isbn", 5, 10, null, 1L);

			when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

			BookEntity result = classUnderTest.getBookById(1L);

			assertEquals(book, result);
		}

		@Test
		void bookDoesNotExist_ThrowsException() {
			when(bookRepository.findById(1L)).thenReturn(Optional.empty());

			assertThrows(BookDoesNotExistException.class, () -> classUnderTest.getBookById(1L));
		}
	}

	@Nested
	class UpdateBookTest {

		@Test
		void updateSuccessful_SavesBook() {
			BookEntity book = new BookEntity(1L, "title", "author", "isbn", 5, 10, null, 1L);

			classUnderTest.updateBook(book);

			verify(bookRepository).save(book);
		}

		@Test
		void optimisticLockFails_ThrowsException() {
			BookEntity book = new BookEntity(1L, "title", "author", "isbn", 5, 10, null, 1L);

			doThrow(ObjectOptimisticLockingFailureException.class).when(bookRepository).save(book);

			assertThrows(DataChangedException.class, () -> classUnderTest.updateBook(book));
		}
	}
}
