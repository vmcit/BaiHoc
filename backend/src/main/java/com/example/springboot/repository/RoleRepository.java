package com.example.springboot.repository;

import com.example.springboot.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Tìm role bằng tên
     */
    Optional<Role> findByRoleName(String roleName);
    
    /**
     * Kiểm tra role có tồn tại bằng tên không
     */
    boolean existsByRoleName(String roleName);
}

