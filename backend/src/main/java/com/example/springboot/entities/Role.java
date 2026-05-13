package com.example.springboot.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Role Entity - Đại diện cho các vai trò trong hệ thống
 * 
 * Ví dụ: ADMIN, USER, MODERATOR...
 * 
 * TABLE: tbl_roles
 * ┌─────────┬─────────────────┬──────────┐
 * │ id      │ role_name       │ status   │
 * ├─────────┼─────────────────┼──────────┤
 * │ 1       │ ADMIN           │ ACTIVE   │
 * │ 2       │ USER            │ ACTIVE   │
 * │ 3       │ MODERATOR       │ ACTIVE   │
 * └─────────┴─────────────────┴──────────┘
 */
@Entity
@Table(name = "tbl_roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    private String roleName;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();
    
    // Many-to-Many relationship với Permission
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(
            name = "tbl_role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
    
    // Many-to-Many relationship với User
    @ManyToMany(mappedBy = "roles", fetch = jakarta.persistence.FetchType.LAZY)
    private Set<User> users = new HashSet<>();
    
    // ═══════════════════════════════════════════════════════════════════════════
    // Constructors
    // ═══════════════════════════════════════════════════════════════════════════
    
    public Role() {
    }
    
    public Role(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
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
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    
    public Set<Permission> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    
    public Set<User> getUsers() {
        return users;
    }
    
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

