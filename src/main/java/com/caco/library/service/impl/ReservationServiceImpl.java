package com.caco.library.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import com.caco.library.service.ReservationService;

import jakarta.transaction.Transactional;

import static com.caco.library.utils.LibraryConstants.MAXIMUM_ACTIVE_RESERVATIONS;

@Service
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	private final LibraryUserService libraryUserService;
	private final BookService bookService;

	@Autowired
	public ReservationServiceImpl(
			ReservationRepository reservationRepository,
			LibraryUserService libraryUserService,
			BookService bookService
	) {
		this.reservationRepository = reservationRepository;
		this.libraryUserService = libraryUserService;
		this.bookService = bookService;
	}

	@Override
	@Transactional // Used to prevent saving information in case one of the calls fail
	public ReservationEntity createReservation(ReservationRequest reservationRequest) {
		LibraryUserEntity libraryUserEntity = libraryUserService.checkLibraryUser(reservationRequest.getLibraryUserId());
		BookEntity bookEntity = bookService.checkBook(reservationRequest.getBookId());

		checkActiveReservationsLimit(reservationRequest.getLibraryUserId());

		// Create reservation
		ReservationEntity reservationEntity = new ReservationEntity();
		reservationEntity.setLibraryUserEntity(libraryUserEntity);
		reservationEntity.setBookEntity(bookEntity);
		reservationEntity.setCreatedAt(LocalDateTime.now());
		reservationEntity.setStatus(ReservationStatus.ACTIVE);

		// Update book availability
		bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() - 1);
		bookService.updateBook(bookEntity);

		return reservationRepository.save(reservationEntity);
	}

	private void checkActiveReservationsLimit(Long libraryUserId) {
		long activeReservations = reservationRepository.countByLibraryUserEntityIdAndStatus(libraryUserId, ReservationStatus.ACTIVE);
		if (activeReservations >= MAXIMUM_ACTIVE_RESERVATIONS) {
			throw new ReservationLimitExceededException();
		}
	}

	@Override
	public ReservationEntity getReservationById(Long reservationId) {
		return reservationRepository.findById(reservationId).orElseThrow(ReservationDoesNotExistException::new);
	}

	@Override
	public List<ReservationEntity> getReservationsByLibraryUserId(Long libraryUserId) {
		libraryUserService.checkLibraryUser(libraryUserId);
		return reservationRepository.findByLibraryUserEntityId(libraryUserId);
	}

	@Override
	@Transactional // Used to prevent saving information in case one of the calls fail
	public ReservationEntity cancelReservation(Long reservationId) {
		ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
				.orElseThrow(ReservationDoesNotExistException::new);

		checkReservationIsNotActive(reservationEntity);

		reservationEntity.setStatus(ReservationStatus.CANCELED);
		reservationRepository.save(reservationEntity);

		BookEntity bookEntity = reservationEntity.getBookEntity();
		bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() + 1);
		bookService.updateBook(bookEntity);
		return reservationEntity;
	}

	private void checkReservationIsNotActive(ReservationEntity reservationEntity) {
		if (reservationEntity.getStatus() != ReservationStatus.ACTIVE) {
			throw new InvalidReservationStateException();
		}
	}
}
