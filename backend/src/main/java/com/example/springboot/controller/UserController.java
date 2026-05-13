package com.example.springboot.controller;

import com.example.springboot.dto.*;
import com.example.springboot.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController - API cho đăng ký, quên mật khẩu, reset mật khẩu
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final AuthService authService;
    
    public UserController(AuthService authService) {
        this.authService = authService;
    }
    
    /**
     * API Đăng ký tài khoản
     * 
     * Ví dụ request:
     * {
     *   "username": "newuser",
     *   "email": "newuser@example.com",
     *   "password": "password123",
     *   "confirmPassword": "password123",
     *   "fullName": "New User",
     *   "phone": "0987654321"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Username is required"));
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Email is required"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Password is required"));
            }
            
            RegisterResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Register successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Register failed: " + e.getMessage()));
        }
    }
    
    /**
     * API Quên mật khẩu - Gửi email reset
     * 
     * Ví dụ request:
     * {
     *   "email": "user@example.com"
     * }
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Email is required"));
            }
            
            authService.forgotPassword(request);
            
            return ResponseEntity.ok(ApiResponse.success(
                    "Password reset email has been sent. Please check your email."
            ));
        } catch (Exception e) {
            // Không reveal nếu email tồn tại hay không (bảo mật)
            return ResponseEntity.ok(ApiResponse.success(
                    "If the email exists, a password reset link has been sent."
            ));
        }
    }
    
    /**
     * API Reset mật khẩu - Dùng mã gửi trong email
     * 
     * Ví dụ request:
     * {
     *   "resetToken": "abc123def456...",
     *   "newPassword": "newpassword123",
     *   "confirmPassword": "newpassword123"
     * }
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            if (request.getResetToken() == null || request.getResetToken().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Reset token is required"));
            }
            if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("New password is required"));
            }
            
            authService.resetPassword(request);
            
            return ResponseEntity.ok(ApiResponse.success(
                    "Password reset successful. You can now login with your new password."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Password reset failed: " + e.getMessage()));
        }
    }
}

