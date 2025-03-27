package com.caco.library.service.impl;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.caco.library.exception.UserDoesNotExistException;
import com.caco.library.exception.UsernameAlreadyTakenException;
import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.repository.LibraryUserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryUserServiceImplTest {

	@Mock
	private LibraryUserRepository libraryUserRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private LibraryUserServiceImpl classUnderTest;

	@Nested
	class FindByUsernameTest {

		@Test
		void existingUser_ReturnsUser() {
			String username = "john";
			LibraryUserEntity entity = new LibraryUserEntity(1L, username, "encoded", "john@mail.com", null);

			when(libraryUserRepository.findByUsername(username)).thenReturn(Optional.of(entity));

			LibraryUserEntity result = classUnderTest.findByUsername(username);

			assertEquals(entity, result);
		}

		@Test
		void userNotFound_ThrowsException() {
			when(libraryUserRepository.findByUsername("john")).thenReturn(Optional.empty());

			assertThrows(UserDoesNotExistException.class, () -> classUnderTest.findByUsername("john"));
		}
	}

	@Nested
	class CheckLibraryUserTest {

		@Test
		void existingUser_ReturnsUser() {
			Long id = 1L;
			LibraryUserEntity entity = new LibraryUserEntity(id, "john", "encoded", "john@mail.com", null);

			when(libraryUserRepository.findById(id)).thenReturn(Optional.of(entity));

			LibraryUserEntity result = classUnderTest.checkLibraryUser(id);

			assertEquals(entity, result);
		}

		@Test
		void userNotFound_ThrowsException() {
			when(libraryUserRepository.findById(1L)).thenReturn(Optional.empty());

			assertThrows(UserDoesNotExistException.class, () -> classUnderTest.checkLibraryUser(1L));
		}
	}

	@Nested
	class RegisterLibraryUserTest {

		@Test
		void validRequest_ReturnsSavedUser() {
			CreateUserRequest request = new CreateUserRequest("john", "pass", "john@mail.com");
			LibraryUserEntity savedEntity = new LibraryUserEntity(null, "john", "encoded", "john@mail.com", null);

			when(passwordEncoder.encode("pass")).thenReturn("encoded");
			when(libraryUserRepository.save(any(LibraryUserEntity.class))).thenReturn(savedEntity);

			LibraryUserEntity result = classUnderTest.registerLibraryUser(request);

			assertEquals(savedEntity, result);
		}

		@Test
		void duplicateUser_ThrowsUsernameAlreadyTaken() {
			CreateUserRequest request = new CreateUserRequest("john", "pass", "john@mail.com");

			when(passwordEncoder.encode("pass")).thenReturn("encoded");
			when(libraryUserRepository.save(any(LibraryUserEntity.class))).thenThrow(DataIntegrityViolationException.class);

			assertThrows(UsernameAlreadyTakenException.class, () -> classUnderTest.registerLibraryUser(request));
		}

		@Test
		void unexpectedError_ThrowsIllegalStateException() {
			CreateUserRequest request = new CreateUserRequest("john", "pass", "john@mail.com");

			when(passwordEncoder.encode("pass")).thenReturn("encoded");
			when(libraryUserRepository.save(any(LibraryUserEntity.class))).thenThrow(RuntimeException.class);

			assertThrows(IllegalStateException.class, () -> classUnderTest.registerLibraryUser(request));
		}
	}
}
