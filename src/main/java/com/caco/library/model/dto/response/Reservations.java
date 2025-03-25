package com.caco.library.model.dto.response;

import java.util.List;
import com.caco.library.model.entity.ReservationEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Reservations(
		List<Reservation> reservations
) {
	public static Reservations fromEntity(List<ReservationEntity> entities) {
		return new Reservations(
				entities.stream().map(Reservation::fromEntity).toList()
		);
	}
}