package com.smartlearn.config;

import com.smartlearn.entity.User;
import com.smartlearn.entity.UserRole;
import com.smartlearn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrapConfig {

    @Bean
    CommandLineRunner bootstrapAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${bootstrap.admin.email:}") String email,
            @Value("${bootstrap.admin.password:}") String password) {
        return args -> {
            if (email.isBlank() || password.isBlank()) {
                return;
            }

            if (userRepository.existsByEmail(email)) {
                return;
            }

            User admin = new User(
                    "System Administrator",
                    email,
                    passwordEncoder.encode(password),
                    UserRole.ADMIN);
            userRepository.save(admin);
        };
    }
}
