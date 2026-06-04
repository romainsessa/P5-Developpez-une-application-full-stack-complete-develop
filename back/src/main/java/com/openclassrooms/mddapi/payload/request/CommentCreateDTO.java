package com.openclassrooms.mddapi.payload.request;

import jakarta.validation.constraints.NotNull;

public record CommentCreateDTO(@NotNull String content) {
}
