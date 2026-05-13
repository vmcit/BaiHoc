package com.example.springboot.repository;

import com.example.springboot.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    /**
     * Tìm permission bằng tên
     */
    Optional<Permission> findByPermissionName(String permissionName);
    
    /**
     * Kiểm tra permission có tồn tại bằng tên không
     */
    boolean existsByPermissionName(String permissionName);
}

