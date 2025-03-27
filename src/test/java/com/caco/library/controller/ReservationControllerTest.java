package com.caco.library.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import com.caco.library.model.dto.request.ReservationRequest;
import com.caco.library.model.dto.response.Reservation;
import com.caco.library.model.dto.response.Reservations;
import com.caco.library.model.entity.BookEntity;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.model.entity.ReservationStatus;
import com.caco.library.service.ReservationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

	@Mock
	private ReservationService reservationService;

	@InjectMocks
	private ReservationController classUnderTest;

	@Nested
	class ReserveBookTest {
		@Test
		void validRequest_ReturnsCreatedReservation() {
			ReservationRequest request = new ReservationRequest(1L, 2L);
			ReservationEntity entity = new ReservationEntity(1L, new LibraryUserEntity(), new BookEntity(), LocalDateTime.now(), null, ReservationStatus.ACTIVE);

			when(reservationService.createReservation(request)).thenReturn(entity);

			ResponseEntity<Reservation> response = classUnderTest.reserveBook(request);

			assertEquals(201, response.getStatusCode().value());
			Reservation reservation = response.getBody();
			assertEquals(entity.getId(), reservation.reservationId());
			assertNotNull(reservation.user());
			assertNotNull(reservation.book());
			assertEquals(entity.getCreatedAt(), reservation.createdAt());
			assertEquals(entity.getExpiresAt(), reservation.expiresAt());
			assertEquals(entity.getStatus().name(), reservation.status());
		}
	}

	@Nested
	class GetReservationByIdTest {

		@Test
		void existingReservation_ReturnsReservation() {
			ReservationEntity entity = new ReservationEntity(1L, new LibraryUserEntity(), new BookEntity(), LocalDateTime.now(), null, ReservationStatus.ACTIVE);

			when(reservationService.getReservationById(1L)).thenReturn(entity);

			ResponseEntity<Reservation> response = classUnderTest.getReservationById(1L);

			assertEquals(200, response.getStatusCode().value());
			Reservation reservation = response.getBody();
			assertEquals(entity.getId(), reservation.reservationId());
			assertNotNull(reservation.user());
			assertNotNull(reservation.book());
			assertEquals(entity.getCreatedAt(), reservation.createdAt());
			assertEquals(entity.getExpiresAt(), reservation.expiresAt());
			assertEquals(entity.getStatus().name(), reservation.status());
		}
	}

	@Nested
	class GetReservationsByUserTest {

		@Test
		void userWithReservations_ReturnsList() {
			ReservationEntity entity = new ReservationEntity(1L, new LibraryUserEntity(), new BookEntity(), LocalDateTime.now(), null, ReservationStatus.ACTIVE);
			List<ReservationEntity> reservations = List.of(entity);

			when(reservationService.getReservationsByLibraryUserId(1L)).thenReturn(reservations);

			ResponseEntity<Reservations> response = classUnderTest.getReservationsByUser(1L);

			assertEquals(200, response.getStatusCode().value());
			assertEquals(1, response.getBody().reservations().size());

			Reservation reservation = response.getBody().reservations().get(0);
			assertEquals(entity.getId(), reservation.reservationId());
			assertNotNull(reservation.user());
			assertNotNull(reservation.book());
			assertEquals(entity.getCreatedAt(), reservation.createdAt());
			assertEquals(entity.getExpiresAt(), reservation.expiresAt());
			assertEquals(entity.getStatus().name(), reservation.status());
		}
	}

	@Nested
	class CancelReservationTest {

		@Test
		void validReservation_ReturnsCanceled() {
			ReservationEntity entity = new ReservationEntity(1L, new LibraryUserEntity(), new BookEntity(), LocalDateTime.now(), null, ReservationStatus.CANCELED);

			when(reservationService.cancelReservation(1L)).thenReturn(entity);

			ResponseEntity<Reservation> response = classUnderTest.cancelReservation(1L);

			assertEquals(200, response.getStatusCode().value());
			assertEquals(ReservationStatus.CANCELED.name(), response.getBody().status());

			Reservation reservation = response.getBody();
			assertEquals(entity.getId(), reservation.reservationId());
			assertNotNull(reservation.user());
			assertNotNull(reservation.book());
			assertEquals(entity.getCreatedAt(), reservation.createdAt());
			assertEquals(entity.getExpiresAt(), reservation.expiresAt());
			assertEquals(entity.getStatus().name(), reservation.status());
		}
	}
}
