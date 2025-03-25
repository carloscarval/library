package com.caco.library.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Authentication(Long libraryUserId, String jwt, String message) {
}
