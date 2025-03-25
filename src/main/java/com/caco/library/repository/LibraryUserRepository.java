package com.caco.library.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.caco.library.model.entity.LibraryUserEntity;

public interface LibraryUserRepository extends JpaRepository<LibraryUserEntity, Long> {

	Optional<LibraryUserEntity> findByUsername(String username);
}
