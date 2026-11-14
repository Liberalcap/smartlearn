package com.smartlearn.dto;

public record LoginResponse(
        String token,
        UserResponse user
) {
}
