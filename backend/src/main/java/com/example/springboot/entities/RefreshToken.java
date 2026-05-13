package com.example.springboot.entities;

import jakarta.persistence.*;

/**
 * RefreshToken Entity - Lưu trữ Refresh Token để làm mới Access Token
 *
 * TABLE: tbl_refresh_tokens
 * ┌──────────┬──────────────────────────────────┬─────────┬──────────────────────────┬──────────────┬────────────┬────────────┬────────────┐
 * │ id       │ uid                              │ user_id │ refresh_token            │ expires_at   │ is_revoked │ created_at │ updated_at │
 * ├──────────┼──────────────────────────────────┼─────────┼──────────────────────────┼──────────────┼────────────┼────────────┼────────────┤
 * │ 1        │ a1b2c3d4e5f6789012345678abcdef90 │ 1       │ eyJhbGciOiJIUzI1NiIs... │ 1700000000   │ false      │ 1600000000 │ 1600000000 │
 * │ 2        │ b2c3d4e5f6789012345678abcdef9012 │ 1       │ eyJhbGciOiJIUzI1NiIs... │ 1700100000   │ true       │ 1600100000 │ 1600200000 │
 * │ 3        │ c3d4e5f6789012345678abcdef901234 │ 2       │ eyJhbGciOiJIUzI1NiIs... │ 1700200000   │ false      │ 1600200000 │ 1600200000 │
 * └──────────┴──────────────────────────────────┴─────────┴──────────────────────────┴──────────────┴────────────┴────────────┴────────────┘
 *
 * Indexes:
 *   - idx_refresh_token_uid        → lookup nhanh bằng uid (không cần load JWT string)
 *   - idx_refresh_token_user_id    → tìm tất cả token của 1 user
 *   - idx_refresh_token_expires_at → cleanup token hết hạn nhanh
 *
 * Ghi chú:
 *   - uid       : 32-char hex (UUID không dấu gạch), dùng để identify token nhẹ hơn JWT string
 *   - is_revoked: set true khi user logout → token không dùng được nữa dù chưa hết hạn
 *   - expires_at: epoch milliseconds, check hết hạn bằng: expiresAt > System.currentTimeMillis()
 */
@Entity
@Table(name = "tbl_refresh_tokens")
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "uid", nullable = false, unique = true, length = 64)
    private String uid; // UUID để identify token nhanh (format: abc123def456...)
    
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "refresh_token", nullable = false, columnDefinition = "TEXT")
    private String refreshToken;
    
    @Column(name = "expires_at", nullable = false)
    private Long expiresAt; // Thời gian hết hạn của refresh token
    
    @Column(name = "is_revoked", nullable = false)
    private Boolean isRevoked = false; // Nếu user logout, set true
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();
    
    @Column(name = "updated_at")
    private Long updatedAt = System.currentTimeMillis();
    
    // ═══════════════════════════════════════════════════════════════════════════
    // Constructors
    // ═══════════════════════════════════════════════════════════════════════════
    
    public RefreshToken() {
    }
    
    public RefreshToken(User user, String refreshToken, Long expiresAt) {
        this.uid = java.util.UUID.randomUUID().toString().replace("-", ""); // 32-char hex, không dấu gạch
        this.user = user;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.isRevoked = false;
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
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public Long getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public Boolean getIsRevoked() {
        return isRevoked;
    }
    
    public void setIsRevoked(Boolean revoked) {
        isRevoked = revoked;
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
    
    /**
     * Kiểm tra xem token có còn hợp lệ không
     */
    public boolean isValid() {
        return !isRevoked && expiresAt > System.currentTimeMillis();
    }
    
    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", user=" + user.getUsername() +
                ", isRevoked=" + isRevoked +
                ", expiresAt=" + expiresAt +
                '}';
    }
}

