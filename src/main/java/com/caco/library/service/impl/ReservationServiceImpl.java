package com.caco.library.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caco.library.dto.ReservationRequest;
import com.caco.library.exception.BookNotAvailableException;
import com.caco.library.exception.InvalidReservationStateException;
import com.caco.library.exception.ReservationLimitExceededException;
import com.caco.library.exception.ResourceNotFoundException;
import com.caco.library.model.entity.BookEntity;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.model.entity.ReservationStatus;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.repository.BookRepository;
import com.caco.library.repository.ReservationRepository;
import com.caco.library.repository.LibraryUserRepository;
import com.caco.library.service.ReservationService;

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
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

		long activeReservations = reservationRepository.countByLibraryUserEntityIdAndStatus(reservationRequest.getUserId(), ReservationStatus.ACTIVE);
		if (activeReservations >= 3) {
			throw new ReservationLimitExceededException("O usuário já possui 3 reservas ACTIVEs");
		}

		BookEntity bookEntity = bookRepository.findById(reservationRequest.getBookId())
				.orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

		if (bookEntity.getAvailableCopies() <= 0) {
			throw new BookNotAvailableException("Livro não disponível para reserva");
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
		ReservationEntity reservationEntity = null;
		try {
			reservationEntity = reservationRepository.findById(reservationId)
					.orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada"));
		} catch (ResourceNotFoundException e) {
			throw new RuntimeException(e);
		}

		if (reservationEntity.getStatus() != ReservationStatus.ACTIVE) {
			throw new InvalidReservationStateException("A reserva não está ACTIVE e não pode ser cancelada");
		}

		reservationEntity.setStatus(ReservationStatus.CANCELED);
		reservationRepository.save(reservationEntity);

		BookEntity bookEntity = reservationEntity.getBookEntity();
		bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() + 1);
		bookRepository.save(bookEntity);
		return null;
	}
}
