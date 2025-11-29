package com.flogin.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderRunner(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String raw = "123";
        String hash = passwordEncoder.encode(raw);
        System.out.println("BCrypt for '123' = " + hash);
    }
}
