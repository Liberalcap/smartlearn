package com.smartlearn.controller;

import com.smartlearn.dto.ApiResponse;
import com.smartlearn.dto.UserResponse;
import com.smartlearn.entity.User;
import com.smartlearn.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        return ApiResponse.success("Current user retrieved successfully", UserResponse.from(user));
    }
}
