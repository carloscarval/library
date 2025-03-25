package com.caco.library.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.caco.library.model.dto.request.ReservationRequest;
import com.caco.library.model.dto.response.Reservation;
import com.caco.library.model.dto.response.Reservations;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.service.ReservationService;

import lombok.RequiredArgsConstructor;

import static com.caco.library.utils.LibraryConstants.API_BASE_URL;
import static com.caco.library.utils.LibraryConstants.API_LIBRARY_USER_ID;
import static com.caco.library.utils.LibraryConstants.API_RESERVATIONS;
import static com.caco.library.utils.LibraryConstants.API_RESERVATION_ID;
import static com.caco.library.utils.LibraryConstants.API_USERS;

@RestController
@RequestMapping(API_BASE_URL + API_RESERVATIONS)
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	// 1. Reserve a book.
	@PostMapping
	public ResponseEntity<Reservation> reserveBook(@RequestBody ReservationRequest request) {
		ReservationEntity reservationEntity = reservationService.createReservation(request);
		return new ResponseEntity<>(Reservation.fromEntity(reservationEntity), HttpStatus.CREATED);
	}

	// 2. Retrieve an existing reservation by ID.
	@GetMapping(API_RESERVATION_ID)
	public ResponseEntity<Reservation> getReservationById(@PathVariable Long reservationId) {
		ReservationEntity reservationEntity = reservationService.getReservationById(reservationId).get();
		return ResponseEntity.ok(Reservation.fromEntity(reservationEntity));
	}

	// 3. Retrieve all reservations for a specific user.
	@GetMapping(API_USERS + API_LIBRARY_USER_ID)
	public ResponseEntity<Reservations> getReservationsByUser(@PathVariable Long libraryUserId) {
		List<ReservationEntity> reservationEntities = reservationService.getReservationsByLibraryUserId(libraryUserId);
		Reservations reservationResponses = new Reservations(reservationEntities.stream()
				.map(Reservation::fromEntity)
				.toList());
		return ResponseEntity.ok(reservationResponses);
	}

	// 4. Cancel a reservation.
	@DeleteMapping(API_RESERVATION_ID)
	public ResponseEntity<Reservation> cancelReservation(@PathVariable Long reservationId) {
		ReservationEntity reservationEntity = reservationService.cancelReservation(reservationId);
		return ResponseEntity.ok(Reservation.fromEntity(reservationEntity));
	}
}
