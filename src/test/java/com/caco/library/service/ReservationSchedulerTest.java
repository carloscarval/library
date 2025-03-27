package com.caco.library.service;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.caco.library.model.entity.BookEntity;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.model.entity.ReservationStatus;
import com.caco.library.repository.ReservationRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationSchedulerTest {

	@Mock
	private ReservationRepository reservationRepository;

	@InjectMocks
	private ReservationScheduler classUnderTest;

	@Nested
	class ExpireOldReservationsTest {

		@Test
		void activeReservationsOlderThanSevenDays_AreMarkedExpiredAndSaved() {
			LocalDateTime eightDaysAgo = LocalDateTime.now().minusDays(8);
			LibraryUserEntity user = new LibraryUserEntity(1L, "john", "123", "john@mail.com", eightDaysAgo);
			BookEntity book = new BookEntity(1L, "book", "author", "isbn", 3, 3, eightDaysAgo, 1L);

			ReservationEntity res1 = new ReservationEntity(1L, user, book, eightDaysAgo, null, ReservationStatus.ACTIVE);
			ReservationEntity res2 = new ReservationEntity(2L, user, book, eightDaysAgo, null, ReservationStatus.ACTIVE);
			List<ReservationEntity> reservations = List.of(res1, res2);

			when(reservationRepository.findByStatusAndCreatedAtBefore(eq(ReservationStatus.ACTIVE), any()))
					.thenReturn(reservations);

			classUnderTest.expireOldReservations();

			verify(reservationRepository).saveAll(reservations);
			assertEquals(ReservationStatus.EXPIRED, res1.getStatus());
			assertEquals(ReservationStatus.EXPIRED, res2.getStatus());
		}
	}
}
