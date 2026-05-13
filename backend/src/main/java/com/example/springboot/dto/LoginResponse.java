package com.example.springboot.dto;

import java.util.Set;

/**
 * DTO cho response Login - Trả về khi login thành công
 */
public class LoginResponse {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn; // Thời gian hết hạn của access token (milliseconds)
    private Set<String> roles;
    private Set<String> permissions;

    public LoginResponse() {
    }

    public LoginResponse(Long userId, String username, String email, String fullName,
                        String accessToken, String refreshToken, Long expiresIn,
                        Set<String> roles, Set<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.roles = roles;
        this.permissions = permissions;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Getters & Setters
    // ═══════════════════════════════════════════════════════════════════════════

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}

