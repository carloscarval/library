package com.caco.library.service;

import java.util.List;
import java.util.Optional;

import com.caco.library.dto.ReservationRequest;
import com.caco.library.exception.ResourceNotFoundException;
import com.caco.library.model.entity.ReservationEntity;

public interface ReservationService {

	ReservationEntity createReservation(ReservationRequest reservationRequest) throws ResourceNotFoundException;

	Optional<ReservationEntity> getReservationById(Long reservationId);

	List<ReservationEntity> getReservationsByLibraryUserId(Long libraryUserId);

	ReservationEntity cancelReservation(Long reservationId);
}
