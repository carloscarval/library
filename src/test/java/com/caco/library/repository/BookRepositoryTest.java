package com.caco.library.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.caco.library.model.entity.BookEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookRepositoryTest {

	@Autowired
	private BookRepository classUnderTest;

	private BookEntity savedBook;

	@BeforeEach
	void setup() {
		BookEntity book = new BookEntity(null, "Test Book", "Test Author", "123-abc", 5, 5, LocalDateTime.now(), 1L);
		savedBook = classUnderTest.save(book);
	}

	@Test
	void findById_ReturnsBook() {
		Optional<BookEntity> result = classUnderTest.findById(savedBook.getId());

		assertTrue(result.isPresent());
		BookEntity resultBookEntity = result.get();

		assertEquals(1L, resultBookEntity.getId());
		assertEquals(savedBook.getTitle(), resultBookEntity.getTitle());
		assertEquals(savedBook.getAuthor(), resultBookEntity.getAuthor());
		assertEquals(savedBook.getIsbn(), resultBookEntity.getIsbn());
		assertEquals(savedBook.getAvailableCopies(), resultBookEntity.getAvailableCopies());
		assertEquals(savedBook.getTotalCopies(), resultBookEntity.getTotalCopies());
		assertEquals(savedBook.getVersion(), resultBookEntity.getVersion());
	}

	@Test
	void findById_NotFound_ReturnsEmpty() {
		Optional<BookEntity> result = classUnderTest.findById(999L);

		assertTrue(result.isEmpty());
	}
}
