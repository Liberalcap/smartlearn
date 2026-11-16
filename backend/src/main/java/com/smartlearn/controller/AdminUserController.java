package com.smartlearn.controller;

import com.smartlearn.dto.AdminUserRequest;
import com.smartlearn.dto.ApiResponse;
import com.smartlearn.dto.UserResponse;
import com.smartlearn.entity.User;
import com.smartlearn.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createManagedUser(
            @Valid @RequestBody AdminUserRequest request) {
        User user = userService.createManagedUser(
                request.name(),
                request.email(),
                request.password(),
                request.role());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Managed user created successfully", UserResponse.from(user)));
    }
}
