package com.example.springboot.repository;

import com.example.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Tìm user bằng username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Tìm user bằng email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Kiểm tra user có tồn tại bằng username không
     */
    boolean existsByUsername(String username);
    
    /**
     * Kiểm tra user có tồn tại bằng email không
     */
    boolean existsByEmail(String email);

    /**
     * Tìm user bằng số điện thoại
     */
    Optional<User> findByPhone(String phone);
}

