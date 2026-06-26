package com.openclassrooms.mddapi.dto;

import java.time.LocalDate;

public record CommentDTO(Long id, String content, LocalDate createdAt, String authorName) {
}
