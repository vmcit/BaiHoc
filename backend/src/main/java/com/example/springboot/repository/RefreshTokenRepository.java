package com.example.springboot.repository;

import com.example.springboot.entities.RefreshToken;
import com.example.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /** Tìm bằng token string (dùng khi validate request) */
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    /** Tìm bằng uid (nhẹ hơn - dùng khi chỉ cần check status) */
    Optional<RefreshToken> findByUid(String uid);

    /** Lấy tất cả tokens của user */
    List<RefreshToken> findByUser(User user);

    /** Lấy tokens còn active của user (chưa bị revoke) */
    List<RefreshToken> findByUserAndIsRevokedFalse(User user);

    /** Xóa tất cả tokens của user */
    void deleteByUser(User user);

    /** Xóa token đã hết hạn - dùng cho scheduled cleanup */
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expiresAt < :now")
    int deleteExpiredTokens(long now);
}

