package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;

public record TopicDTO(Long id, @NotBlank String name, @NotBlank String description) {
}
