package com.caco.library.security.service;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.repository.LibraryUserRepository;

import static com.caco.library.utils.LibraryMessages.USER_DOES_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	private LibraryUserRepository libraryUserRepository;

	@InjectMocks
	private CustomUserDetailsService classUnderTest;

	@Nested
	class LoadUserByUsernameTest {

		@Test
		void existingUser_ReturnsUserDetails() {
			LibraryUserEntity entity = new LibraryUserEntity(1L, "john", "encoded", "john@mail.com", null);

			when(libraryUserRepository.findByUsername("john")).thenReturn(Optional.of(entity));

			UserDetails result = classUnderTest.loadUserByUsername("john");

			assertNotNull(result);
			assertEquals("john", result.getUsername());
			assertEquals("encoded", result.getPassword());
		}

		@Test
		void userNotFound_ThrowsException() {
			when(libraryUserRepository.findByUsername("john")).thenReturn(Optional.empty());

			assertThrows(
					UsernameNotFoundException.class,
					() -> classUnderTest.loadUserByUsername("john"),
					USER_DOES_NOT_EXIST
			);
		}
	}
}
