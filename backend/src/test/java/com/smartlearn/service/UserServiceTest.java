package com.smartlearn.service;

import com.smartlearn.entity.User;
import com.smartlearn.entity.UserRole;
import com.smartlearn.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserHashesPasswordBeforeSaving() {
        when(userRepository.existsByEmail("student@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.registerUser(
                "Student",
                "student@example.com",
                "password123",
                UserRole.STUDENT);

        assertEquals("hashed-password", user.getPassword());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUserRejectsDuplicateEmail() {
        when(userRepository.existsByEmail("student@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(
                        "Student",
                        "student@example.com",
                        "password123",
                        UserRole.STUDENT));

        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUserRejectsPrivilegedRole() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(
                        "Administrator",
                        "admin@example.com",
                        "password123",
                        UserRole.ADMIN));

        assertEquals("Public registration is limited to STUDENT accounts", exception.getMessage());
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUsesPasswordEncoderToVerifyPassword() {
        User user = new User(
                "Student",
                "student@example.com",
                "hashed-password",
                UserRole.STUDENT);
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq("password123"), eq("hashed-password"))).thenReturn(true);

        User result = userService.login("student@example.com", "password123");

        assertEquals("student@example.com", result.getEmail());
        verify(passwordEncoder).matches("password123", "hashed-password");
    }
}
