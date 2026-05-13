package com.example.springboot.config;

import com.example.springboot.entities.Permission;
import com.example.springboot.entities.Role;
import com.example.springboot.entities.User;
import com.example.springboot.repository.PermissionRepository;
import com.example.springboot.repository.RoleRepository;
import com.example.springboot.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * DataLoader - Khởi tạo dữ liệu ban đầu (Users, Roles, Permissions)
 */
@Configuration
public class DataLoader {
    
    @Bean
    CommandLineRunner initializeData(UserRepository userRepository,
                                    RoleRepository roleRepository,
                                    PermissionRepository permissionRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                    INITIALIZING DATABASE WITH DEFAULT DATA                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝\n");
            
            // 1. Tạo Permissions
            System.out.println("📝 Creating Permissions...");
            Permission createUser = createPermissionIfNotExists(permissionRepository, 
                    "CREATE_USER", "Quyền tạo user mới");
            Permission editUser = createPermissionIfNotExists(permissionRepository, 
                    "EDIT_USER", "Quyền chỉnh sửa user");
            Permission deleteUser = createPermissionIfNotExists(permissionRepository, 
                    "DELETE_USER", "Quyền xóa user");
            Permission viewReport = createPermissionIfNotExists(permissionRepository, 
                    "VIEW_REPORT", "Quyền xem báo cáo");
            Permission viewProfile = createPermissionIfNotExists(permissionRepository, 
                    "VIEW_PROFILE", "Quyền xem hồ sơ người dùng");
            Permission editProfile = createPermissionIfNotExists(permissionRepository, 
                    "EDIT_PROFILE", "Quyền chỉnh sửa hồ sơ cá nhân");
            System.out.println("✓ Permissions created successfully\n");
            
            // 2. Tạo Roles
            System.out.println("👥 Creating Roles...");
            
            // ADMIN role
            Role adminRole = null;
            if (!roleRepository.existsByRoleName("ADMIN")) {
                adminRole = new Role("ADMIN", "Vai trò quản trị viên");
                Set<Permission> adminPermissions = new HashSet<>();
                adminPermissions.add(createUser);
                adminPermissions.add(editUser);
                adminPermissions.add(deleteUser);
                adminPermissions.add(viewReport);
                adminPermissions.add(viewProfile);
                adminPermissions.add(editProfile);
                adminRole.setPermissions(adminPermissions);
                adminRole = roleRepository.save(adminRole);
                System.out.println("  ✓ ADMIN role created with " + adminPermissions.size() + " permissions");
            } else {
                adminRole = roleRepository.findByRoleName("ADMIN").get();
                System.out.println("  ✓ ADMIN role already exists");
            }
            
            // USER role
            Role userRole = null;
            if (!roleRepository.existsByRoleName("USER")) {
                userRole = new Role("USER", "Vai trò người dùng bình thường");
                Set<Permission> userPermissions = new HashSet<>();
                userPermissions.add(viewProfile);
                userPermissions.add(editProfile);
                userRole.setPermissions(userPermissions);
                userRole = roleRepository.save(userRole);
                System.out.println("  ✓ USER role created with " + userPermissions.size() + " permissions");
            } else {
                userRole = roleRepository.findByRoleName("USER").get();
                System.out.println("  ✓ USER role already exists");
            }
            
            // MODERATOR role
            Role moderatorRole = null;
            if (!roleRepository.existsByRoleName("MODERATOR")) {
                moderatorRole = new Role("MODERATOR", "Vai trò người quản lý nội dung");
                Set<Permission> modPermissions = new HashSet<>();
                modPermissions.add(viewReport);
                modPermissions.add(viewProfile);
                modPermissions.add(editProfile);
                moderatorRole.setPermissions(modPermissions);
                moderatorRole = roleRepository.save(moderatorRole);
                System.out.println("  ✓ MODERATOR role created with " + modPermissions.size() + " permissions");
            } else {
                moderatorRole = roleRepository.findByRoleName("MODERATOR").get();
                System.out.println("  ✓ MODERATOR role already exists");
            }
            System.out.println();
            
            // 3. Tạo Users
            System.out.println("👤 Creating Users...");
            
            // Admin user
            if (!userRepository.existsByUsername("admin")) {
                User adminUser = new User("admin", "admin@example.com", 
                        passwordEncoder.encode("admin123"));
                adminUser.setFullName("Administrator");
                adminUser.setPhone("0123456789");
                adminUser.setStatus("ACTIVE");
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                adminUser.setRoles(adminRoles);
                userRepository.save(adminUser);
                System.out.println("  ✓ Admin user created (username: admin, password: admin123)");
            } else {
                System.out.println("  ✓ Admin user already exists");
            }
            
            // Regular user 1
            if (!userRepository.existsByUsername("user1")) {
                User regularUser1 = new User("user1", "user1@example.com", 
                        passwordEncoder.encode("user1234"));
                regularUser1.setFullName("User One");
                regularUser1.setPhone("0123456790");
                regularUser1.setStatus("ACTIVE");
                Set<Role> userRoles = new HashSet<>();
                userRoles.add(userRole);
                regularUser1.setRoles(userRoles);
                userRepository.save(regularUser1);
                System.out.println("  ✓ Regular user1 created (username: user1, password: user1234)");
            } else {
                System.out.println("  ✓ User1 already exists");
            }
            
            // Regular user 2
            if (!userRepository.existsByUsername("user2")) {
                User regularUser2 = new User("user2", "user2@example.com", 
                        passwordEncoder.encode("user2234"));
                regularUser2.setFullName("User Two");
                regularUser2.setPhone("0123456791");
                regularUser2.setStatus("ACTIVE");
                Set<Role> userRoles = new HashSet<>();
                userRoles.add(userRole);
                regularUser2.setRoles(userRoles);
                userRepository.save(regularUser2);
                System.out.println("  ✓ Regular user2 created (username: user2, password: user2234)");
            } else {
                System.out.println("  ✓ User2 already exists");
            }
            
            // Moderator user
            if (!userRepository.existsByUsername("moderator")) {
                User modUser = new User("moderator", "moderator@example.com", 
                        passwordEncoder.encode("mod12345"));
                modUser.setFullName("Moderator User");
                modUser.setPhone("0123456792");
                modUser.setStatus("ACTIVE");
                Set<Role> modRoles = new HashSet<>();
                modRoles.add(moderatorRole);
                modUser.setRoles(modRoles);
                userRepository.save(modUser);
                System.out.println("  ✓ Moderator user created (username: moderator, password: mod12345)");
            } else {
                System.out.println("  ✓ Moderator user already exists");
            }
            
            System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                   DATABASE INITIALIZATION COMPLETED SUCCESSFULLY              ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════════╝\n");
            System.out.println("📌 Test Credentials:");
            System.out.println("   ADMIN:     admin / admin123");
            System.out.println("   USER:      user1 / user1234");
            System.out.println("   USER:      user2 / user2234");
            System.out.println("   MODERATOR: moderator / mod12345\n");
        };
    }
    
    /**
     * Helper method - Tạo permission nếu chưa tồn tại
     */
    private Permission createPermissionIfNotExists(PermissionRepository repository, 
                                                   String name, String description) {
        if (!repository.existsByPermissionName(name)) {
            Permission permission = new Permission(name, description);
            return repository.save(permission);
        }
        return repository.findByPermissionName(name).get();
    }
}

