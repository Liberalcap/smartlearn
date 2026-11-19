package com.smartlearn.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LessonRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull @Min(1) Integer position
) {
}
