package com.caco.library.model.dto.response;

import java.time.LocalDateTime;
import com.caco.library.model.entity.ReservationEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Reservation(
		Long reservationId,
		LibraryUser user,
		Book book,
		LocalDateTime createdAt,
		LocalDateTime expiresAt,
		String status
) {
	public static Reservation fromEntity(ReservationEntity entity) {
		return new Reservation(
				entity.getId(),
				LibraryUser.fromEntity(entity.getLibraryUserEntity()),
				Book.fromEntity(entity.getBookEntity()),
				entity.getCreatedAt(),
				entity.getExpiresAt(),
				entity.getStatus().name()
		);
	}
}