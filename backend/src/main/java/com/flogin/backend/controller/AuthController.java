package com.flogin.backend.controller;

import com.flogin.backend.dto.LoginRequest;
import com.flogin.backend.dto.LoginResponse;
import com.flogin.backend.entity.User;
import com.flogin.backend.security.JwtUtil;
import com.flogin.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            User user = authService.login(request);

            String token = jwtUtil.generateToken(user);

            LoginResponse response = LoginResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .username(user.getUsername())
                    .token(token)
                    .build();

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            LoginResponse response = LoginResponse.builder()
                    .success(false)
                    .message(ex.getMessage())   // "Invalid username or password"
                    .build();

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
    }
}
