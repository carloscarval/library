package com.caco.library.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.model.entity.ReservationStatus;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

	List<ReservationEntity> findByLibraryUserEntityId(Long id);

	long countByLibraryUserEntityIdAndStatus(Long id, ReservationStatus status);

	List<ReservationEntity> findByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime createdAt);
}
