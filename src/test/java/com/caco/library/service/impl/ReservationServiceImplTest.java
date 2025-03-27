package com.caco.library.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.caco.library.exception.BookNotAvailableException;
import com.caco.library.exception.InvalidReservationStateException;
import com.caco.library.exception.ReservationDoesNotExistException;
import com.caco.library.exception.ReservationLimitExceededException;
import com.caco.library.model.dto.request.ReservationRequest;
import com.caco.library.model.entity.BookEntity;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.model.entity.ReservationStatus;
import com.caco.library.repository.ReservationRepository;
import com.caco.library.service.BookService;
import com.caco.library.service.LibraryUserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

	@Mock
	private ReservationRepository reservationRepository;

	@Mock
	private LibraryUserService libraryUserService;

	@Mock
	private BookService bookService;

	@InjectMocks
	private ReservationServiceImpl classUnderTest;

	@Nested
	class CreateReservationTest {

		@Test
		void validRequest_ReturnsSavedReservation() {
			ReservationRequest request = new ReservationRequest(1L, 2L);
			LibraryUserEntity user = new LibraryUserEntity(1L, "user", "pass", "user@mail.com", null);
			BookEntity book = new BookEntity(2L, "title", "author", "isbn", 5, 10, null, 1L);
			ReservationEntity saved = new ReservationEntity(1L, user, book, LocalDateTime.now(), null, ReservationStatus.ACTIVE);

			when(libraryUserService.checkLibraryUser(1L)).thenReturn(user);
			when(bookService.getBookById(2L)).thenReturn(book);
			when(reservationRepository.countByLibraryUserEntityIdAndStatus(1L, ReservationStatus.ACTIVE)).thenReturn(0L);
			when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(saved);

			ReservationEntity result = classUnderTest.createReservation(request);

			assertEquals(saved, result);
		}

		@Test
		void bookUnavailable_ThrowsException() {
			ReservationRequest request = new ReservationRequest(1L, 2L);
			BookEntity book = new BookEntity(2L, "title", "author", "isbn", 0, 10, null, 1L);
			LibraryUserEntity user = new LibraryUserEntity(1L, "user", "pass", "user@mail.com", null);

			when(libraryUserService.checkLibraryUser(1L)).thenReturn(user);
			when(bookService.getBookById(2L)).thenReturn(book);

			assertThrows(BookNotAvailableException.class, () -> classUnderTest.createReservation(request));
		}

		@Test
		void tooManyActiveReservations_ThrowsException() {
			ReservationRequest request = new ReservationRequest(1L, 2L);
			BookEntity book = new BookEntity(2L, "title", "author", "isbn", 5, 10, null, 1L);
			LibraryUserEntity user = new LibraryUserEntity(1L, "user", "pass", "user@mail.com", null);

			when(libraryUserService.checkLibraryUser(1L)).thenReturn(user);
			when(bookService.getBookById(2L)).thenReturn(book);
			when(reservationRepository.countByLibraryUserEntityIdAndStatus(1L, ReservationStatus.ACTIVE)).thenReturn(3L);

			assertThrows(ReservationLimitExceededException.class, () -> classUnderTest.createReservation(request));
		}
	}

	@Nested
	class GetReservationByIdTest {

		@Test
		void reservationExists_ReturnsReservation() {
			ReservationEntity reservation = new ReservationEntity(1L, null, null, null, null, ReservationStatus.ACTIVE);

			when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

			ReservationEntity result = classUnderTest.getReservationById(1L);

			assertEquals(reservation, result);
		}

		@Test
		void reservationDoesNotExist_ThrowsException() {
			when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

			assertThrows(ReservationDoesNotExistException.class, () -> classUnderTest.getReservationById(1L));
		}
	}

	@Nested
	class GetReservationsByLibraryUserIdTest {

		@Test
		void returnsListOfReservations() {
			LibraryUserEntity user = new LibraryUserEntity(1L, "user", "pass", "user@mail.com", null);
			List<ReservationEntity> reservations = List.of(
					new ReservationEntity(1L, user, null, null, null, ReservationStatus.ACTIVE)
			);

			when(libraryUserService.checkLibraryUser(1L)).thenReturn(user);
			when(reservationRepository.findByLibraryUserEntityId(1L)).thenReturn(reservations);

			List<ReservationEntity> result = classUnderTest.getReservationsByLibraryUserId(1L);

			assertEquals(reservations, result);
		}
	}

	@Nested
	class CancelReservationTest {

		@Test
		void validReservation_UpdatesStatusAndBook() {
			BookEntity book = new BookEntity(2L, "title", "author", "isbn", 1, 10, null, 1L);
			ReservationEntity reservation = new ReservationEntity(1L, null, book, null, null, ReservationStatus.ACTIVE);

			when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
			when(reservationRepository.save(any())).thenReturn(reservation);

			ReservationEntity result = classUnderTest.cancelReservation(1L);

			assertEquals(ReservationStatus.CANCELED, result.getStatus());
		}

		@Test
		void reservationNotActive_ThrowsException() {
			ReservationEntity reservation = new ReservationEntity(1L, null, null, null, null, ReservationStatus.EXPIRED);

			when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

			assertThrows(InvalidReservationStateException.class, () -> classUnderTest.cancelReservation(1L));
		}
	}
}
