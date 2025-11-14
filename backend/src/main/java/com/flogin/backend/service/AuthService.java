package com.flogin.backend.service;

import com.flogin.backend.dto.LoginRequest;
import com.flogin.backend.entity.User;
import com.flogin.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean login(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .map(user -> user.getPassword().equals(request.getPassword()))
                .orElse(false);
    }
}
