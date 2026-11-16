package com.smartlearn.service;

import com.smartlearn.entity.User;
import com.smartlearn.entity.UserRole;
import com.smartlearn.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String name, String email, String password, UserRole role) {
        if (role != UserRole.STUDENT) {
            throw new IllegalArgumentException("Public registration is limited to STUDENT accounts");
        }

        validateNewEmail(email);
        User user = new User(name, email, passwordEncoder.encode(password), UserRole.STUDENT);
        return userRepository.save(user);
    }

    public User createManagedUser(String name, String email, String password, UserRole role) {
        if (role == UserRole.STUDENT) {
            throw new IllegalArgumentException("Use public registration for STUDENT accounts");
        }

        validateNewEmail(email);
        User user = new User(name, email, passwordEncoder.encode(password), role);
        return userRepository.save(user);
    }

    private void validateNewEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }
}
