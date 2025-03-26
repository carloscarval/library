package com.caco.library.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.caco.library.model.entity.ReservationEntity;
import com.caco.library.model.entity.ReservationStatus;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

	@Query("""
			    SELECT r FROM ReservationEntity r
			    JOIN FETCH r.bookEntity
			    JOIN FETCH r.libraryUserEntity
			    WHERE r.libraryUserEntity.id = :id
			""")
	List<ReservationEntity> findByLibraryUserEntityId(@Param("id") Long id);

	long countByLibraryUserEntityIdAndStatus(Long id, ReservationStatus status);

	List<ReservationEntity> findByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime createdAt);
}
