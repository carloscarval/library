package com.caco.library.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.caco.library.model.entity.LibraryUserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LibraryUserRepositoryTest {

	@Autowired
	private LibraryUserRepository classUnderTest;

	private LibraryUserEntity savedUser;

	@BeforeEach
	void setup() {
		LibraryUserEntity user = new LibraryUserEntity(
				null,
				"john",
				"pass123",
				"john@mail.com",
				LocalDateTime.now()
		);
		savedUser = classUnderTest.save(user);
	}

	@Test
	void findByUsername_ReturnsUser() {
		Optional<LibraryUserEntity> result = classUnderTest.findByUsername("john");

		assertTrue(result.isPresent());
		LibraryUserEntity foundUser = result.get();

		assertEquals(savedUser.getId(), foundUser.getId());
		assertEquals(savedUser.getUsername(), foundUser.getUsername());
		assertEquals(savedUser.getPassword(), foundUser.getPassword());
		assertEquals(savedUser.getEmail(), foundUser.getEmail());
		assertEquals(savedUser.getCreatedAt(), foundUser.getCreatedAt());
	}

	@Test
	void findByUsername_NotFound_ReturnsEmpty() {
		Optional<LibraryUserEntity> result = classUnderTest.findByUsername("invalid");

		assertTrue(result.isEmpty());
	}
}
