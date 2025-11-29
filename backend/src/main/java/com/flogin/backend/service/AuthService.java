package com.flogin.backend.service;

import com.flogin.backend.dto.LoginRequest;
import com.flogin.backend.entity.User;
import com.flogin.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Xử lý đăng nhập:
     * - Tìm user theo username
     * - So sánh password raw với hash trong DB bằng passwordEncoder.matches()
     * - Ném exception nếu sai (controller sẽ map sang 401)
     */
    public User login(LoginRequest request) {
        String username = request.getUsername();
        String rawPassword = request.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }
}
