package com.caco.library.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String author;

	@Column(unique = true)
	private String isbn;

	@Column(name = "available_copies")
	private int availableCopies;

	@Column(name = "total_copies")
	private int totalCopies;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Version
	private Long version;
}
