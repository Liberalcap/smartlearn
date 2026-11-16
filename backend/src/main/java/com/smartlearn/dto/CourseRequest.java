package com.smartlearn.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
        @NotBlank String title,
        @NotBlank String description
) {
}
