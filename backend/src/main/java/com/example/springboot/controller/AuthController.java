package com.example.springboot.controller;

import com.example.springboot.dto.ApiResponse;
import com.example.springboot.dto.LoginRequest;
import com.example.springboot.dto.LoginResponse;
import com.example.springboot.dto.RefreshTokenRequest;
import com.example.springboot.security.JwtTokenProvider;
import com.example.springboot.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - API cho đăng nhập, đăng xuất, làm mới token
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    /**
     * API Đăng nhập
     * 
     * Ví dụ request:
     * {
     *   "username": "admin",
     *   "password": "admin123"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Username is required"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Password is required"));
            }
            
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }
    
    /**
     * API Làm mới Access Token
     * 
     * Ví dụ request:
     * {
     *   "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
     * }
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            if (request.getRefreshToken() == null || request.getRefreshToken().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Refresh token is required"));
            }
            
            LoginResponse response = authService.refreshAccessToken(request);
            return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Refresh token failed: " + e.getMessage()));
        }
    }
    
    /**
     * API Đăng xuất
     * 
     * Header: Authorization: Bearer {accessToken}
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Authorization header is missing or invalid"));
            }
            
            String token = authHeader.substring(7);
            
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid token"));
            }
            
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            authService.logout(userId);
            
            return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Logout failed: " + e.getMessage()));
        }
    }
    
    /**
     * API Kiểm tra token hợp lệ
     * 
     * Header: Authorization: Bearer {accessToken}
     */
    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Authorization header is missing or invalid"));
            }
            
            String token = authHeader.substring(7);
            
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Token is invalid or expired"));
            }
            
            String username = jwtTokenProvider.getUsernameFromToken(token);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            long expiresIn = jwtTokenProvider.getTokenExpirationTime(token);
            
            return ResponseEntity.ok(ApiResponse.success(
                    java.util.Map.of(
                            "username", username,
                            "userId", userId,
                            "expiresIn", expiresIn,
                            "message", "Token is valid"
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token validation failed: " + e.getMessage()));
        }
    }
}

