package com.caco.library.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.model.entity.ReservationStatus;
import com.caco.library.repository.ReservationRepository;

@Service
public class ReservationScheduler {

	private final ReservationRepository reservationRepository;

	public ReservationScheduler(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Scheduled(cron = "0 0 0 * * ?") // Executes daily at midnight
	@Transactional
	public void expireOldReservations() {
		LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
		List<ReservationEntity> oldReservations = reservationRepository
				.findByStatusAndCreatedAtBefore(ReservationStatus.ACTIVE, sevenDaysAgo);

		for (ReservationEntity reservation : oldReservations) {
			reservation.setStatus(ReservationStatus.EXPIRED);
		}
		reservationRepository.saveAll(oldReservations);
	}
}
