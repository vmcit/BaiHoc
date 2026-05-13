package com.example.springboot.repository;

import com.example.springboot.entities.PasswordResetToken;
import com.example.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /** Tìm bằng token string */
    Optional<PasswordResetToken> findByResetToken(String resetToken);

    /** Tìm bằng uid (nhẹ hơn - dùng khi chỉ cần check status) */
    Optional<PasswordResetToken> findByUid(String uid);

    /** Lấy tất cả reset tokens của user */
    List<PasswordResetToken> findByUser(User user);

    /** Tìm reset token còn hiệu lực (chưa dùng, chưa hết hạn) */
    Optional<PasswordResetToken> findByUserAndIsUsedFalse(User user);

    /** Xóa token đã hết hạn - dùng cho scheduled cleanup */
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiresAt < :now")
    int deleteExpiredTokens(long now);
}

