package com.caco.library.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.caco.library.exception.BookNotAvailableException;
import com.caco.library.exception.InvalidReservationStateException;
import com.caco.library.exception.ReservationLimitExceededException;
import com.caco.library.exception.ResourceNotFoundException;
import com.caco.library.model.dto.request.ReservationRequest;
import com.caco.library.model.entity.BookEntity;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.model.entity.ReservationStatus;
import com.caco.library.repository.BookRepository;
import com.caco.library.repository.LibraryUserRepository;
import com.caco.library.repository.ReservationRepository;
import com.caco.library.service.ReservationService;

import static com.caco.library.utils.LibraryMessages.BOOK_DOES_NOT_EXIST;
import static com.caco.library.utils.LibraryMessages.BOOK_NOT_AVAILABLE;
import static com.caco.library.utils.LibraryMessages.RESERVATION_NOT_ACTIVE;
import static com.caco.library.utils.LibraryMessages.RESERVATION_NOT_FOUND;
import static com.caco.library.utils.LibraryMessages.USER_HAS_THREE_ACTIVE_RESERVATIONS;
import static com.caco.library.utils.LibraryMessages.USER_NOT_FOUND;

@Service
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	private final LibraryUserRepository libraryUserRepository;
	private final BookRepository bookRepository;

	@Autowired
	public ReservationServiceImpl(
			ReservationRepository reservationRepository,
			LibraryUserRepository libraryUserRepository,
			BookRepository bookRepository
	) {
		this.reservationRepository = reservationRepository;
		this.libraryUserRepository = libraryUserRepository;
		this.bookRepository = bookRepository;
	}

	@Override
	public ReservationEntity createReservation(ReservationRequest reservationRequest) {

		LibraryUserEntity libraryUserEntity = libraryUserRepository.findById(reservationRequest.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

		long activeReservations = reservationRepository.countByLibraryUserEntityIdAndStatus(reservationRequest.getUserId(), ReservationStatus.ACTIVE);
		if (activeReservations >= 3) {
			throw new ReservationLimitExceededException(USER_HAS_THREE_ACTIVE_RESERVATIONS);
		}

		BookEntity bookEntity = bookRepository.findById(reservationRequest.getBookId())
				.orElseThrow(() -> new ResourceNotFoundException(BOOK_DOES_NOT_EXIST));

		if (bookEntity.getAvailableCopies() <= 0) {
			throw new BookNotAvailableException(BOOK_NOT_AVAILABLE);
		}

		ReservationEntity reservationEntity = new ReservationEntity();
		reservationEntity.setLibraryUserEntity(libraryUserEntity);
		reservationEntity.setBookEntity(bookEntity);
		reservationEntity.setCreatedAt(LocalDateTime.now());
		reservationEntity.setStatus(ReservationStatus.ACTIVE);

		bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() - 1);
		bookRepository.save(bookEntity);

		return reservationRepository.save(reservationEntity);
	}

	@Override
	public Optional<ReservationEntity> getReservationById(Long reservationId) {
		return reservationRepository.findById(reservationId);
	}

	@Override
	public List<ReservationEntity> getReservationsByLibraryUserId(Long libraryUserId) {
		return reservationRepository.findByLibraryUserEntityId(libraryUserId);
	}

	@Override
	public ReservationEntity cancelReservation(Long reservationId) {
		ReservationEntity reservationEntity;
		try {
			reservationEntity = reservationRepository.findById(reservationId)
					.orElseThrow(() -> new ResourceNotFoundException(RESERVATION_NOT_FOUND));
		} catch (ResourceNotFoundException e) {
			throw new RuntimeException(e);
		}

		if (reservationEntity.getStatus() != ReservationStatus.ACTIVE) {
			throw new InvalidReservationStateException(RESERVATION_NOT_ACTIVE);
		}

		reservationEntity.setStatus(ReservationStatus.CANCELED);
		reservationRepository.save(reservationEntity);

		BookEntity bookEntity = reservationEntity.getBookEntity();
		bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() + 1);
		bookRepository.save(bookEntity);
		return null;
	}
}
