package com.caco.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caco.library.model.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
	// Métodos adicionais, se necessário
}
