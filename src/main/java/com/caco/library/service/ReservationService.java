package com.caco.library.service;

import java.util.List;
import com.caco.library.model.dto.request.ReservationRequest;
import com.caco.library.model.entity.ReservationEntity;

public interface ReservationService {

	ReservationEntity createReservation(ReservationRequest reservationRequest);

	ReservationEntity getReservationById(Long reservationId);

	List<ReservationEntity> getReservationsByLibraryUserId(Long libraryUserId);

	ReservationEntity cancelReservation(Long reservationId);
}
