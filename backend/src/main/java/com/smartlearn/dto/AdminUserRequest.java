package com.smartlearn.dto;

import com.smartlearn.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull UserRole role
) {
}
