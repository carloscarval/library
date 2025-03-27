package com.caco.library.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.caco.library.model.entity.BookEntity;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.model.entity.ReservationStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ReservationRepositoryTest {

	@Autowired
	private ReservationRepository classUnderTest;

	@Autowired
	private LibraryUserRepository libraryUserRepository;

	@Autowired
	private BookRepository bookRepository;

	private LibraryUserEntity user;
	private BookEntity book;

	private ReservationEntity reservation1;
	private ReservationEntity reservation2;

	@BeforeEach
	void setup() {
		user = libraryUserRepository.save(
				new LibraryUserEntity(null, "john", "pass", "john@mail.com", LocalDateTime.now())
		);

		book = bookRepository.save(
				new BookEntity(null, "book", "author", "isbn", 3, 3, LocalDateTime.now(), 1L)
		);

		reservation1 = classUnderTest.save(
				new ReservationEntity(null, user, book, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(5), ReservationStatus.ACTIVE)
		);

		reservation2 = classUnderTest.save(
				new ReservationEntity(null, user, book, LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5), ReservationStatus.EXPIRED)
		);
	}

	@Test
	void findByLibraryUserEntityId_ReturnsReservations() {
		List<ReservationEntity> result = classUnderTest.findByLibraryUserEntityId(user.getId());

		assertEquals(2, result.size());

		for (ReservationEntity res : result) {
			assertEquals(user.getId(), res.getLibraryUserEntity().getId());
			assertEquals(user.getUsername(), res.getLibraryUserEntity().getUsername());
			assertEquals(user.getPassword(), res.getLibraryUserEntity().getPassword());
			assertEquals(user.getEmail(), res.getLibraryUserEntity().getEmail());
			assertEquals(user.getCreatedAt(), res.getLibraryUserEntity().getCreatedAt());

			assertEquals(book.getId(), res.getBookEntity().getId());
			assertEquals(book.getTitle(), res.getBookEntity().getTitle());
			assertEquals(book.getAuthor(), res.getBookEntity().getAuthor());
			assertEquals(book.getIsbn(), res.getBookEntity().getIsbn());
			assertEquals(book.getAvailableCopies(), res.getBookEntity().getAvailableCopies());
			assertEquals(book.getTotalCopies(), res.getBookEntity().getTotalCopies());
			assertEquals(book.getCreatedAt(), res.getBookEntity().getCreatedAt());
			assertEquals(book.getVersion(), res.getBookEntity().getVersion());

			assertNotNull(res.getCreatedAt());
			assertNotNull(res.getExpiresAt());
			assertNotNull(res.getStatus());
		}
	}

	@Test
	void countByLibraryUserEntityIdAndStatus_ReturnsActiveCount() {
		long count = classUnderTest.countByLibraryUserEntityIdAndStatus(user.getId(), ReservationStatus.ACTIVE);
		assertEquals(1, count);
	}

	@Test
	void findByStatusAndCreatedAtBefore_ReturnsExpired() {
		List<ReservationEntity> result = classUnderTest.findByStatusAndCreatedAtBefore(
				ReservationStatus.EXPIRED, LocalDateTime.now().minusDays(1)
		);

		assertEquals(1, result.size());

		ReservationEntity res = result.get(0);
		assertEquals(ReservationStatus.EXPIRED, res.getStatus());

		assertEquals(user.getId(), res.getLibraryUserEntity().getId());
		assertEquals(book.getId(), res.getBookEntity().getId());

		assertEquals(reservation2.getCreatedAt(), res.getCreatedAt());
		assertEquals(reservation2.getExpiresAt(), res.getExpiresAt());
	}
}
