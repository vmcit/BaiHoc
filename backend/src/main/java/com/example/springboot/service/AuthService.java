package com.example.springboot.service;

import com.example.springboot.dto.*;
import com.example.springboot.entities.*;
import com.example.springboot.repository.*;
import com.example.springboot.security.JwtTokenProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AuthService - Xử lý logic xác thực người dùng
 */
@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    public AuthService(UserRepository userRepository,
                      RefreshTokenRepository refreshTokenRepository,
                      PasswordResetTokenRepository passwordResetTokenRepository,
                      RoleRepository roleRepository,
                      JwtTokenProvider jwtTokenProvider,
                      PasswordEncoder passwordEncoder,
                      EmailService emailService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }
    
    /**
     * Đăng nhập
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // Tìm user bằng username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Kiểm tra status
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("User account is not active");
        }
        
        // Kiểm tra password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Tăng login attempts
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            if (user.getLoginAttempts() >= 5) {
                user.setStatus("LOCKED");
            }
            userRepository.save(user);
            throw new RuntimeException("Invalid password");
        }
        
        // Reset login attempts khi login thành công
        user.setLoginAttempts(0);
        user.setLastLogin(System.currentTimeMillis());
        userRepository.save(user);
        
        // Lấy roles trước để nhúng vào Access Token
        Set<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        // Tạo Access Token (có roles trong claims)
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getId(), roles);
        
        // Tạo Refresh Token
        String refreshTokenString = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getId());
        
        // Lưu Refresh Token vào database
        RefreshToken refreshToken = new RefreshToken(
                user,
                refreshTokenString,
                System.currentTimeMillis() + 604800000 // 7 days
        );
        refreshTokenRepository.save(refreshToken);
        
        // Lấy permissions từ tất cả roles
        Set<String> permissions = new HashSet<>();
        for (Role role : user.getRoles()) {
            permissions.addAll(role.getPermissions().stream()
                    .map(Permission::getPermissionName)
                    .collect(Collectors.toSet()));
        }
        
        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                accessToken,
                refreshTokenString,
                jwtTokenProvider.getTokenExpirationTime(accessToken),
                roles,
                permissions
        );
    }
    
    /**
     * Làm mới Access Token bằng Refresh Token
     */
    @Transactional
    public LoginResponse refreshAccessToken(RefreshTokenRequest request) {
        String refreshTokenString = request.getRefreshToken();
        
        // Kiểm tra refresh token hợp lệ
        if (!jwtTokenProvider.validateToken(refreshTokenString)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }
        
        // Tìm refresh token trong database
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenString)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        
        // Kiểm tra token có bị revoke không
        if (refreshToken.getIsRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }
        
        User user = refreshToken.getUser();
        
        // Kiểm tra status user
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("User account is not active");
        }
        
        // Tạo Access Token mới
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getId());
        
        // Lấy roles
        Set<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
        
        // Lấy permissions
        Set<String> permissions = new HashSet<>();
        for (Role role : user.getRoles()) {
            permissions.addAll(role.getPermissions().stream()
                    .map(Permission::getPermissionName)
                    .collect(Collectors.toSet()));
        }
        
        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                newAccessToken,
                refreshTokenString,
                jwtTokenProvider.getTokenExpirationTime(newAccessToken),
                roles,
                permissions
        );
    }
    
    /**
     * Đăng xuất - Revoke tất cả refresh tokens
     */
    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Revoke tất cả refresh tokens của user
        java.util.List<RefreshToken> tokens = refreshTokenRepository.findByUserAndIsRevokedFalse(user);
        long now = System.currentTimeMillis();
        tokens.forEach(token -> {
            token.setIsRevoked(true);
            token.setUpdatedAt(now);
        });
        refreshTokenRepository.saveAll(tokens); // 1 lần thay vì N lần
    }
    
    /**
     * Revoke một refresh token cụ thể
     */
    @Transactional
    public void revokeRefreshToken(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenString)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        
        refreshToken.setIsRevoked(true);
        refreshToken.setUpdatedAt(System.currentTimeMillis());
        refreshTokenRepository.save(refreshToken);
    }
    
    /**
     * Đăng ký tài khoản mới
     */
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // Validation
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        if (request.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        
        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Tạo user mới
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        user.setFullName(request.getFullName() != null ? request.getFullName() : request.getUsername());
        user.setPhone(request.getPhone());
        user.setStatus("ACTIVE");
        
        // Gán role USER mặc định
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found. Please initialize roles first."));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        
        // Gửi email chào mừng (không chặn nếu lỗi)
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getUsername(), user.getFullName());
        } catch (Exception e) {
            System.err.println("Warning: Could not send welcome email: " + e.getMessage());
        }
        
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                "Register successful. You can now login."
        );
    }
    
    /**
     * Yêu cầu reset mật khẩu (gửi email với mã)
     */
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        // Tìm user bằng email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User with this email not found"));
        
        // Tạo reset token (mã 6 chữ số hoặc 32 ký tự ngẫu nhiên)
        String resetToken = RandomStringUtils.randomAlphanumeric(32);
        
        // Xóa token cũ nếu có
        java.util.List<PasswordResetToken> oldTokens = passwordResetTokenRepository.findByUser(user);
        for (PasswordResetToken token : oldTokens) {
            if (!token.getIsUsed()) {
                passwordResetTokenRepository.delete(token);
            }
        }
        
        // Tạo PasswordResetToken mới (hết hạn sau 15 phút = 900000ms)
        PasswordResetToken resetTokenEntity = new PasswordResetToken(
                user,
                resetToken,
                System.currentTimeMillis() + 900000 // 15 minutes
        );
        passwordResetTokenRepository.save(resetTokenEntity);
        
        // Gửi email với mã reset
        emailService.sendPasswordResetEmail(user.getEmail(), user.getUsername(), resetToken);
    }
    
    /**
     * Reset mật khẩu bằng token
     */
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // Validation
        if (request.getResetToken() == null || request.getResetToken().trim().isEmpty()) {
            throw new RuntimeException("Reset token is required");
        }
        if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            throw new RuntimeException("New password is required");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        if (request.getNewPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        
        // Tìm reset token
        PasswordResetToken resetToken = passwordResetTokenRepository.findByResetToken(request.getResetToken())
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));
        
        // Kiểm tra token hợp lệ
        if (!resetToken.isValid()) {
            throw new RuntimeException("Reset token expired or already used");
        }
        
        // Update mật khẩu user
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(System.currentTimeMillis());
        userRepository.save(user);
        
        // Đánh dấu token đã dùng
        resetToken.setIsUsed(true);
        passwordResetTokenRepository.save(resetToken);
        
        // Gửi email xác nhận
        try {
            emailService.sendPasswordResetSuccessEmail(user.getEmail(), user.getUsername());
        } catch (Exception e) {
            System.err.println("Warning: Could not send confirmation email: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Đăng nhập bằng SĐT + OTP
    // ─────────────────────────────────────────────────────────────────

    /**
     * Xác thực SĐT + mật khẩu (bước 1 - trước khi gửi OTP)
     */
    public void verifyPhoneAndPassword(String phone, String password) {
        String normalizedPhone = normalizePhone(phone);
        User user = userRepository.findByPhone(normalizedPhone)
                .orElseThrow(() -> new RuntimeException("Số điện thoại chưa được đăng ký"));

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("Tài khoản đã bị khóa hoặc không hoạt động");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }
    }

    /**
     * Tạo JWT sau khi OTP đã xác minh thành công (bước 2)
     */
    @Transactional
    public LoginResponse loginByPhone(String phone) {
        User user = userRepository.findByPhone(normalizePhone(phone))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Reset login attempts
        user.setLoginAttempts(0);
        user.setLastLogin(System.currentTimeMillis());
        userRepository.save(user);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getId(), roles);
        String refreshTokenString = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getId());

        RefreshToken refreshToken = new RefreshToken(
                user, refreshTokenString,
                System.currentTimeMillis() + 604800000L
        );
        refreshTokenRepository.save(refreshToken);

        Set<String> permissions = new HashSet<>();
        for (Role role : user.getRoles()) {
            permissions.addAll(role.getPermissions().stream()
                    .map(Permission::getPermissionName)
                    .collect(Collectors.toSet()));
        }

        return new LoginResponse(
                user.getId(), user.getUsername(), user.getEmail(), user.getFullName(),
                accessToken, refreshTokenString,
                jwtTokenProvider.getTokenExpirationTime(accessToken),
                roles, permissions
        );
    }

    /**
     * Chuẩn hóa số điện thoại về dạng 0xxxxxxxxx
     * +84968965682 → 0968965682
     * +10968965682 → 0968965682
     */
    private String normalizePhone(String phone) {
        if (phone == null) return null;
        String p = phone.trim().replaceAll("[\\s\\-()]", "");
        if (p.startsWith("+84")) return "0" + p.substring(3);
        if (p.startsWith("84") && p.length() == 11) return "0" + p.substring(2);
        // Loại bỏ mã quốc gia +1 (US) nếu browser tự thêm
        if (p.startsWith("+1")) return p.substring(2);
        if (p.startsWith("+")) return p.substring(1); // fallback: bỏ dấu +
        return p;
    }
}
