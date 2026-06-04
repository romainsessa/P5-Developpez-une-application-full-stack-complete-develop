package com.openclassrooms.mddapi.dto;

import java.util.List;
import java.time.LocalDateTime;

public record UserDTO(
    Long id,
    String username,
    String email,
    List<TopicDTO> topics,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
