package com.caco.library.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Link to the user making the reservation.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "library_user_id", nullable = false)
	private LibraryUserEntity libraryUserEntity;

	// Link to the reserved book.
	@ManyToOne(fetch = FetchType.EAGER)
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
