package com.caco.library.model.dto;

import java.time.LocalDateTime;
import com.caco.library.model.entity.LibraryUserEntity;

public record UserResponse(
    Long id,
    String username,
    String email,
    LocalDateTime createdAt
) {
    public static UserResponse fromEntity(LibraryUserEntity entity) {
        return new UserResponse(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getCreatedAt()
        );
    }
}