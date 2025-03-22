package com.caco.library.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Link to the user making the reservation.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "library_user_id", nullable = false)
	private LibraryUserEntity libraryUserEntity;

	// Link to the reserved book.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", nullable = false)
	private BookEntity bookEntity;

	// When the reservation was created.
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	// When the reservation expires (if not picked up).
	@Column(name = "expires_at")
	private LocalDateTime expiresAt;

	// Current status of the reservation (ACTIVE, CANCELED, EXPIRED).
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ReservationStatus status = ReservationStatus.ACTIVE;
}
