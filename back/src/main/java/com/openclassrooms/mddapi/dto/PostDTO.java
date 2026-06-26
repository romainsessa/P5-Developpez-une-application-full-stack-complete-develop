package com.openclassrooms.mddapi.dto;

import java.time.LocalDate;
import java.util.List;

public record PostDTO(Long id, String topic, String title, String content, LocalDate createdAt, String author,
		List<CommentDTO> comments) {
}
