package com.example.springboot.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Permission Entity - Đại diện cho các quyền hạn trong hệ thống
 *
 * Ví dụ: CREATE_USER, EDIT_USER, DELETE_USER, VIEW_REPORT...
 *
 * TABLE: tbl_permissions
 * ┌─────────┬──────────────────────┬──────────┐
 * │ id      │ permission_name      │ status   │
 * ├─────────┼──────────────────────┼──────────┤
 * │ 1       │ CREATE_USER          │ ACTIVE   │
 * │ 2       │ EDIT_USER            │ ACTIVE   │
 * │ 3       │ DELETE_USER          │ ACTIVE   │
 * │ 4       │ VIEW_REPORT          │ ACTIVE   │
 * └─────────┴──────────────────────┴──────────┘
 */
@Entity
@Table(name = "tbl_permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "permission_name", unique = true, nullable = false, length = 100)
    private String permissionName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();

    // Many-to-Many relationship với Role
    @ManyToMany(mappedBy = "permissions", fetch = jakarta.persistence.FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    // ═══════════════════════════════════════════════════════════════════════════
    // Constructors
    // ═══════════════════════════════════════════════════════════════════════════

    public Permission() {
    }

    public Permission(String permissionName, String description) {
        this.permissionName = permissionName;
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

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", permissionName='" + permissionName + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

