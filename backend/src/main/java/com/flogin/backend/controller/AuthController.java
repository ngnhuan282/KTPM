package com.flogin.backend.controller;

import com.flogin.backend.dto.LoginRequest;
import com.flogin.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean isValid = authService.login(request);
        if (isValid) {
            return ResponseEntity.ok().body("Đăng nhập thành công!");
        } else {
            return ResponseEntity.status(401).body("Email hoặc mật khẩu sai!");
        }
    }
}
