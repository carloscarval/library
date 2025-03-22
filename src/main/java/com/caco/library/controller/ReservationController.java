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
import com.caco.library.dto.ReservationRequest;
import com.caco.library.model.dto.ReservationResponse;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.service.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	// 1. Reserve a book.
	@PostMapping
	public ResponseEntity<ReservationResponse> reserveBook(@RequestBody ReservationRequest request) {
		ReservationEntity reservationEntity = reservationService.createReservation(request);
		return new ResponseEntity<>(ReservationResponse.fromEntity(reservationEntity), HttpStatus.CREATED);
	}

	// 2. Retrieve an existing reservation by ID.
	@GetMapping("/{id}")
	public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
		ReservationEntity reservationEntity = reservationService.getReservationById(id).get();
		return ResponseEntity.ok(ReservationResponse.fromEntity(reservationEntity));
	}

	// 3. Retrieve all reservations for a specific user.
	@GetMapping("/user/{libraryUserId}")
	public ResponseEntity<List<ReservationResponse>> getReservationsByUser(@PathVariable Long libraryUserId) {
		List<ReservationEntity> reservationEntities = reservationService.getReservationsByLibraryUserId(libraryUserId);
		List<ReservationResponse> reservationResponses = reservationEntities.stream()
				.map(ReservationResponse::fromEntity)
				.toList();
		return ResponseEntity.ok(reservationResponses);
	}

	// 4. Cancel a reservation.
	@DeleteMapping("/{id}")
	public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id) {
		ReservationEntity reservationEntity = reservationService.cancelReservation(id);
		return ResponseEntity.ok(ReservationResponse.fromEntity(reservationEntity));
	}
}
