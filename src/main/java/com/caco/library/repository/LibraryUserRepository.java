package com.caco.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caco.library.model.entity.LibraryUserEntity;

public interface LibraryUserRepository extends JpaRepository<LibraryUserEntity, Long> {
	// Métodos adicionais, se necessário
}
