package com.smartlearn.controller;

import com.smartlearn.dto.ApiResponse;
import com.smartlearn.dto.LoginRequest;
import com.smartlearn.dto.LoginResponse;
import com.smartlearn.dto.RegisterRequest;
import com.smartlearn.dto.UserResponse;
import com.smartlearn.entity.User;
import com.smartlearn.service.UserService;
import com.smartlearn.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        User user = userService.registerUser(
                request.name(),
                request.email(),
                request.password(),
                request.role());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", UserResponse.from(user)));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.email(), request.password());
        LoginResponse response = new LoginResponse(jwtService.generateToken(user), UserResponse.from(user));
        return ApiResponse.success("Login successful", response);
    }
}
