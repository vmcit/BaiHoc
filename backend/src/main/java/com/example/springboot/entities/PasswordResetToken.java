package com.example.springboot.entities;

import jakarta.persistence.*;

/**
 * PasswordResetToken Entity - Lưu token để reset mật khẩu
 * 
 * TABLE: tbl_password_reset_tokens
 * ┌──────────┬────────────┬──────────────────────────┬──────────────┬────────────┐
 * │ id       │ user_id    │ reset_token              │ expires_at   │ created_at │
 * ├──────────┼────────────┼──────────────────────────┼──────────────┼────────────┤
 * │ 1        │ 1          │ abc123def456...          │ 1700000000   │ 1600000000 │
 * │ 2        │ 2          │ xyz789uvw012...          │ 1700100000   │ 1600100000 │
 * └──────────┴────────────┴──────────────────────────┴──────────────┴────────────┘
 */
@Entity
@Table(name = "tbl_password_reset_tokens")
public class PasswordResetToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "uid", nullable = false, unique = true, length = 64)
    private String uid; // UUID để identify token nhanh
    
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "reset_token", nullable = false, length = 100, unique = true)
    private String resetToken; // Token mã hóa để reset password
    
    @Column(name = "expires_at", nullable = false)
    private Long expiresAt; // Thời gian hết hạn của reset token (15 phút)
    
    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false; // Nếu user đã dùng token này để reset, set true
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();
    
    // ═══════════════════════════════════════════════════════════════════════════
    // Constructors
    // ═══════════════════════════════════════════════════════════════════════════
    
    public PasswordResetToken() {
    }
    
    public PasswordResetToken(User user, String resetToken, Long expiresAt) {
        this.uid = java.util.UUID.randomUUID().toString().replace("-", ""); // 32-char hex, không dấu gạch
        this.user = user;
        this.resetToken = resetToken;
        this.expiresAt = expiresAt;
        this.isUsed = false;
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
    
    public String getResetToken() {
        return resetToken;
    }
    
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    
    public Long getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public Boolean getIsUsed() {
        return isUsed;
    }
    
    public void setIsUsed(Boolean used) {
        isUsed = used;
    }
    
    public Long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Kiểm tra xem token có còn hợp lệ không
     */
    public boolean isValid() {
        return !isUsed && expiresAt > System.currentTimeMillis();
    }
    
    @Override
    public String toString() {
        return "PasswordResetToken{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", user=" + user.getUsername() +
                ", isUsed=" + isUsed +
                ", expiresAt=" + expiresAt +
                '}';
    }
}

