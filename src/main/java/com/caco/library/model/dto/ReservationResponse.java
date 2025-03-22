
package com.caco.library.model.dto;

import java.time.LocalDateTime;
import com.caco.library.model.entity.ReservationEntity;

public record ReservationResponse(
		Long id,
		Long userId,
		Long bookId,
		LocalDateTime createdAt,
		LocalDateTime expiresAt,
		String status
) {
	public static ReservationResponse fromEntity(ReservationEntity entity) {
		return new ReservationResponse(
				entity.getId(),
				entity.getLibraryUserEntity().getId(),
				entity.getBookEntity().getId(),
				entity.getCreatedAt(),
				entity.getExpiresAt(),
				entity.getStatus().name()
		);
	}
}