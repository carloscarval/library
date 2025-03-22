package com.caco.library.model.dto;

import java.time.LocalDateTime;
import com.caco.library.model.entity.BookEntity;

public record BookResponse(
    Long id,
    String title,
    String author,
    String isbn,
    int availableCopies,
    int totalCopies,
    LocalDateTime createdAt
) {
    public static BookResponse fromEntity(BookEntity entity) {
        return new BookResponse(
            entity.getId(),
            entity.getTitle(),
            entity.getAuthor(),
            entity.getIsbn(),
            entity.getAvailableCopies(),
            entity.getTotalCopies(),
            entity.getCreatedAt()
        );
    }
}