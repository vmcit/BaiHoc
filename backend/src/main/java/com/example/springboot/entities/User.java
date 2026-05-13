package com.example.springboot.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User Entity - Đại diện cho người dùng trong hệ thống
 * 
 * TABLE: tbl_users
 * ┌──────────┬──────────────┬────────┬────────────┬─────────────────┬────────────┐
 * │ id       │ username     │ email  │ password   │ status          │ created_at │
 * ├──────────┼──────────────┼────────┼────────────┼─────────────────┼────────────┤
 * │ 1        │ admin        │ ...    │ hashed...  │ ACTIVE          │ 1234567890 │
 * │ 2        │ user1        │ ...    │ hashed...  │ ACTIVE          │ 1234567890 │
 * │ 3        │ user2        │ ...    │ hashed...  │ INACTIVE        │ 1234567890 │
 * └──────────┴──────────────┴────────┴────────────┴─────────────────┴────────────┘
 * 
 * REFRESH TOKEN TABLE: tbl_refresh_tokens
 * ┌──────────┬────────────┬──────────────────┬────────────┬──────────────┐
 * │ id       │ user_id    │ refresh_token    │ expires_at │ created_at   │
 * ├──────────┼────────────┼──────────────────┼────────────┼──────────────┤
 * │ 1        │ 1          │ eyJhbGc...       │ 1234567890 │ 1234567890   │
 * └──────────┴────────────┴──────────────────┴────────────┴──────────────┘
 */
@Entity
@Table(name = "tbl_users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "full_name", length = 200)
    private String fullName;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, LOCKED
    
    @Column(name = "login_attempts", nullable = false)
    private Integer loginAttempts = 0; // Số lần login thất bại
    
    @Column(name = "last_login")
    private Long lastLogin; // Lần đăng nhập cuối cùng
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();
    
    @Column(name = "updated_at")
    private Long updatedAt = System.currentTimeMillis();
    
    // Many-to-Many relationship với Role
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(
            name = "tbl_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    // One-to-Many relationship với RefreshToken
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private Set<RefreshToken> refreshTokens = new HashSet<>();
    
    // ═══════════════════════════════════════════════════════════════════════════
    // Constructors
    // ═══════════════════════════════════════════════════════════════════════════
    
    public User() {
    }
    
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = "ACTIVE";
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // Getters & Setters
    // ═══════════════════════════════════════════════════════════════════════════
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getLoginAttempts() {
        return loginAttempts;
    }
    
    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }
    
    public Long getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Long lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public Long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    
    public Long getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public Set<RefreshToken> getRefreshTokens() {
        return refreshTokens;
    }
    
    public void setRefreshTokens(Set<RefreshToken> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

