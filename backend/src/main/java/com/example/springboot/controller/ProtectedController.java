package com.example.springboot.controller;

import com.example.springboot.dto.ApiResponse;
import com.example.springboot.entities.Role;
import com.example.springboot.entities.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ProtectedController - Demo phân quyền ROLE_ADMIN / ROLE_USER
 *
 * Quy tắc:
 *   ROLE_ADMIN → truy cập tất cả API
 *   ROLE_USER  → chỉ truy cập API sản phẩm, không xem danh sách user
 *
 * Test:
 *   1. Đăng nhập bằng tài khoản admin  → dùng token gọi /api/protected/admin/users
 *   2. Đăng nhập bằng tài khoản user   → gọi /api/protected/admin/users → 403
 *   3. User gọi /api/protected/products → 200 OK
 */
@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public ProtectedController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    // ─── Helper: xác thực token, trả về null nếu invalid ─────────
    private String extractValidToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        return jwtTokenProvider.validateToken(token) ? token : null;
    }
    
    // ═══════════════════════════════════════════════════════════
    // PUBLIC (chỉ cần đăng nhập, không phân biệt role)
    // ═══════════════════════════════════════════════════════════

    /**
     * GET /api/protected/me
     * Xem thông tin của chính mình — mọi user đã đăng nhập đều dùng được
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMe(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractValidToken(authHeader);
        if (token == null) return unauthorized();

        Set<String> roles = jwtTokenProvider.getRolesFromToken(token);
        return ResponseEntity.ok(ApiResponse.success("Thông tin tài khoản", Map.of(
                "userId",   jwtTokenProvider.getUserIdFromToken(token),
                "username", jwtTokenProvider.getUsernameFromToken(token),
                "roles",    roles
        )));
    }

    // ═══════════════════════════════════════════════════════════
    // ROLE_USER trở lên — xem sản phẩm
    // ═══════════════════════════════════════════════════════════

    /**
     * GET /api/protected/products
     * Danh sách sản phẩm — ROLE_USER hoặc ROLE_ADMIN đều xem được
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse> getProducts(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractValidToken(authHeader);
        if (token == null) return unauthorized();

        Set<String> roles = jwtTokenProvider.getRolesFromToken(token);
        if (!roles.contains("ROLE_USER") && !roles.contains("ROLE_ADMIN")) {
            return forbidden("ROLE_USER");
        }

        List<Map<String, Object>> products = List.of(
            Map.of("id", 1, "name", "Laptop Dell XPS 15",      "price", 25_990_000, "category", "Laptop"),
            Map.of("id", 2, "name", "iPhone 15 Pro Max",       "price", 33_990_000, "category", "Điện thoại"),
            Map.of("id", 3, "name", "Samsung Galaxy S24",      "price", 22_990_000, "category", "Điện thoại"),
            Map.of("id", 4, "name", "Tai nghe Sony WH-1000XM5","price",  7_490_000, "category", "Phụ kiện"),
            Map.of("id", 5, "name", "MacBook Air M3",          "price", 28_990_000, "category", "Laptop")
        );
        return ResponseEntity.ok(ApiResponse.success("Danh sách sản phẩm", products));
    }

    // ═══════════════════════════════════════════════════════════
    // ROLE_ADMIN only — quản lý user, thống kê
    // ═══════════════════════════════════════════════════════════

    /**
     * GET /api/protected/admin/users
     * Danh sách tất cả user — CHỈ ROLE_ADMIN
     * ROLE_USER gọi API này → 403 Forbidden
     */
    @GetMapping("/admin/users")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractValidToken(authHeader);
        if (token == null) return unauthorized();

        if (!jwtTokenProvider.hasRole(token, "ROLE_ADMIN")) {
            return forbidden("ROLE_ADMIN");
        }

        List<Map<String, Object>> users = userRepository.findAll().stream()
                .map(u -> Map.<String, Object>of(
                        "id",       u.getId(),
                        "username", u.getUsername(),
                        "email",    u.getEmail(),
                        "fullName", u.getFullName() != null ? u.getFullName() : "",
                        "status",   u.getStatus(),
                        "roles",    u.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Danh sách tất cả user", users));
    }

    /**
     * GET /api/protected/admin/stats
     * Thống kê hệ thống — CHỈ ROLE_ADMIN
     */
    @GetMapping("/admin/stats")
    public ResponseEntity<ApiResponse> getStats(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractValidToken(authHeader);
        if (token == null) return unauthorized();

        if (!jwtTokenProvider.hasRole(token, "ROLE_ADMIN")) {
            return forbidden("ROLE_ADMIN");
        }

        return ResponseEntity.ok(ApiResponse.success("Thống kê hệ thống", Map.of(
                "totalUsers",    4,
                "activeUsers",   3,
                "lockedUsers",   1,
                "totalProducts", 10,
                "totalOrders",   128,
                "revenue",       "1,234,500,000 VND"
        )));
    }

    /**
     * DELETE /api/protected/admin/users/{userId}
     * Xóa user — CHỈ ROLE_ADMIN
     */
    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long userId) {
        String token = extractValidToken(authHeader);
        if (token == null) return unauthorized();

        if (!jwtTokenProvider.hasRole(token, "ROLE_ADMIN")) {
            return forbidden("ROLE_ADMIN");
        }

        // Fake: không xóa thật, chỉ demo response
        return ResponseEntity.ok(ApiResponse.success("Đã xóa user id=" + userId, null));
    }

    // ─── Helper responses ─────────────────────────────────────
    private ResponseEntity<ApiResponse> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Token không hợp lệ hoặc chưa đăng nhập"));
    }

    private ResponseEntity<ApiResponse> forbidden(String requiredRole) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("Bạn không có quyền. Yêu cầu: " + requiredRole));
    }
}

