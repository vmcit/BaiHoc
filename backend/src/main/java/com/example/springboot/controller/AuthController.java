package com.example.springboot.controller;

import com.example.springboot.dto.ApiResponse;
import com.example.springboot.dto.LoginRequest;
import com.example.springboot.dto.LoginResponse;
import com.example.springboot.dto.RefreshTokenRequest;
import com.example.springboot.entities.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.security.JwtTokenProvider;
import com.example.springboot.service.AuthService;
import com.example.springboot.service.OtpService;
import com.example.springboot.service.TotpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - API cho đăng nhập, đăng xuất, làm mới token
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OtpService otpService;
    private final TotpService totpService;
    private final UserRepository userRepository;
    
    public AuthController(AuthService authService,
                          JwtTokenProvider jwtTokenProvider,
                          OtpService otpService,
                          TotpService totpService,
                          UserRepository userRepository) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.otpService = otpService;
        this.totpService = totpService;
        this.userRepository = userRepository;
    }
    
    /**
     * API Đăng nhập
     * 
     * Ví dụ request:
     * {
     *   "username": "admin",
     *   "password": "admin123"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Username is required"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Password is required"));
            }
            
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }
    
    /**
     * API Làm mới Access Token
     * 
     * Ví dụ request:
     * {
     *   "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
     * }
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            if (request.getRefreshToken() == null || request.getRefreshToken().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Refresh token is required"));
            }
            
            LoginResponse response = authService.refreshAccessToken(request);
            return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Refresh token failed: " + e.getMessage()));
        }
    }
    
    /**
     * API Đăng xuất
     * 
     * Header: Authorization: Bearer {accessToken}
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Authorization header is missing or invalid"));
            }
            
            String token = authHeader.substring(7);
            
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid token"));
            }
            
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            authService.logout(userId);
            
            return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Logout failed: " + e.getMessage()));
        }
    }
    
    /**
     * API Kiểm tra token hợp lệ
     * 
     * Header: Authorization: Bearer {accessToken}
     */
    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Authorization header is missing or invalid"));
            }
            
            String token = authHeader.substring(7);
            
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Token is invalid or expired"));
            }
            
            String username = jwtTokenProvider.getUsernameFromToken(token);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            long expiresIn = jwtTokenProvider.getTokenExpirationTime(token);
            
            return ResponseEntity.ok(ApiResponse.success(
                    java.util.Map.of(
                            "username", username,
                            "userId", userId,
                            "expiresIn", expiresIn,
                            "message", "Token is valid"
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token validation failed: " + e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Đăng nhập bằng SĐT + OTP
    // ─────────────────────────────────────────────────────────────────

    /**
     * Bước 1: Xác thực SĐT + mật khẩu → gửi OTP (qua Webhook.site giả lập)
     *
     * Body: { "phone": "0968965682", "password": "123456" }
     */
    @PostMapping("/phone-login")
    public ResponseEntity<ApiResponse> phoneLogin(@RequestBody java.util.Map<String, String> body) {
        try {
            String phone = body.get("phone");
            String password = body.get("password");

            if (phone == null || phone.isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Số điện thoại là bắt buộc"));
            if (password == null || password.isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Mật khẩu là bắt buộc"));

            // Chuẩn hóa SĐT: bỏ khoảng trắng, dấu +, mã quốc gia do browser tự thêm
            phone = phone.trim().replaceAll("[\\s\\-()]", "");
            if (phone.startsWith("+84")) phone = "0" + phone.substring(3);
            else if (phone.startsWith("+1")) phone = phone.substring(2);
            else if (phone.startsWith("+")) phone = phone.substring(1);

            authService.verifyPhoneAndPassword(phone, password);
            otpService.sendOtp(phone);

            return ResponseEntity.ok(ApiResponse.success("Đã gửi OTP",
                    java.util.Map.of("phone", phone)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Bước 2: Xác minh OTP → trả về JWT token nếu đúng
     *
     * Body: { "phone": "0968965682", "otp": "123456" }
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody java.util.Map<String, String> body) {
        try {
            String phone = body.get("phone");
            String otp   = body.get("otp");

            if (phone == null || phone.isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Số điện thoại là bắt buộc"));
            if (otp == null || otp.isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("OTP là bắt buộc"));

            if (!otpService.verifyOtp(phone, otp)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("OTP không đúng hoặc đã hết hạn"));
            }

            LoginResponse response = authService.loginByPhone(phone);
            return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Hộp thư SMS giả lập — mở trình duyệt để xem OTP
     * GET /api/auth/otp-inbox/{phone}
     */
    @GetMapping(value = "/otp-inbox/{phone}", produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> otpInbox(@PathVariable String phone) {
        String otp = otpService.peekOtp(phone);
        String html;
        if (otp == null) {
            html = """
                <!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8">
                <title>Hộp thư SMS</title>
                <style>body{font-family:sans-serif;display:flex;justify-content:center;align-items:center;height:100vh;margin:0;background:#f0f2f5;}
                .box{background:#fff;border-radius:12px;padding:40px 50px;box-shadow:0 4px 20px rgba(0,0,0,.1);text-align:center;}</style>
                </head><body><div class="box">
                <div style="font-size:3rem">📭</div>
                <h2 style="color:#333">Chưa có OTP</h2>
                <p style="color:#888">Số: <b>%s</b></p>
                <p style="color:#aaa;font-size:.85rem">Hãy gửi OTP từ trang đăng nhập trước.</p>
                <button onclick="location.reload()" style="margin-top:1rem;padding:8px 20px;background:#1976d2;color:#fff;border:none;border-radius:6px;cursor:pointer;font-size:1rem">🔄 Làm mới</button>
                </div></body></html>
                """.formatted(phone);
        } else {
            html = """
                <!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8">
                <title>Hộp thư SMS</title>
                <style>body{font-family:sans-serif;display:flex;justify-content:center;align-items:center;height:100vh;margin:0;background:#f0f2f5;}
                .box{background:#fff;border-radius:12px;padding:40px 50px;box-shadow:0 4px 20px rgba(0,0,0,.1);text-align:center;max-width:400px;}
                .otp{font-size:3rem;font-weight:bold;letter-spacing:.5rem;color:#2e7d32;background:#e8f5e9;padding:16px 24px;border-radius:10px;display:inline-block;margin:16px 0;cursor:pointer;border:2px dashed #43a047;}
                .tag{background:#e3f2fd;color:#1565c0;padding:3px 10px;border-radius:20px;font-size:.8rem;}
                </style>
                </head><body><div class="box">
                <div style="font-size:2.5rem">📱</div>
                <h2 style="color:#333;margin:8px 0">Tin nhắn SMS giả lập</h2>
                <span class="tag">Số: %s</span>
                <p style="color:#555;margin-top:16px">Mã OTP của bạn là:</p>
                <div class="otp" onclick="navigator.clipboard.writeText('%s');this.innerText='✅ Đã copy!';setTimeout(()=>this.innerText='%s',1500)" title="Click để copy">%s</div>
                <p style="color:#888;font-size:.85rem">⏱ Hết hạn sau 5 phút — Click vào mã để copy</p>
                <button onclick="location.reload()" style="margin-top:1rem;padding:8px 20px;background:#1976d2;color:#fff;border:none;border-radius:6px;cursor:pointer;font-size:1rem">🔄 Làm mới</button>
                </div></body></html>
                """.formatted(phone, otp, otp, otp);
        }
        return ResponseEntity.ok(html);
    }

    // TOTP / Google Authenticator
    // ─────────────────────────────────────────────────────────────────

    /**
     * Bước 1: Xác thực SĐT + mật khẩu → trả về QR (lần đầu) hoặc yêu cầu nhập mã
     * Body: { "phone": "0936352582", "password": "123456" }
     */
    @PostMapping("/qr-login")
    public ResponseEntity<ApiResponse> qrLogin(@RequestBody java.util.Map<String, String> body) {
        try {
            String phone = body.get("phone");
            String password = body.get("password");

            if (phone == null || phone.isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Số điện thoại là bắt buộc"));
            if (password == null || password.isBlank())
                return ResponseEntity.badRequest().body(ApiResponse.error("Mật khẩu là bắt buộc"));

            phone = phone.trim().replaceAll("[\\s\\-()]", "");
            if (phone.startsWith("+84")) phone = "0" + phone.substring(3);
            else if (phone.startsWith("+1")) phone = phone.substring(2);
            else if (phone.startsWith("+")) phone = phone.substring(1);

            authService.verifyPhoneAndPassword(phone, password);

            User user = userRepository.findByPhone(phone)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            if (!user.getTotpEnabled()) {
                // Lần đầu: tạo secret + QR
                String secret = totpService.generateSecret();
                String qrDataUri = totpService.generateQrDataUri(phone, secret);
                return ResponseEntity.ok(ApiResponse.success("Quét QR để kích hoạt",
                        java.util.Map.of(
                                "status", "SETUP_REQUIRED",
                                "phone", phone,
                                "secret", secret,
                                "qrDataUri", qrDataUri
                        )));
            } else {
                // Đã kích hoạt: yêu cầu nhập mã 6 số
                return ResponseEntity.ok(ApiResponse.success("Nhập mã xác thực",
                        java.util.Map.of(
                                "status", "OTP_REQUIRED",
                                "phone", phone
                        )));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Kích hoạt TOTP lần đầu: xác minh mã đầu tiên → lưu secret, bật totpEnabled → JWT
     * Body: { "phone": "...", "secret": "...", "code": "123456" }
     */
    @PostMapping("/totp/activate")
    public ResponseEntity<ApiResponse> totpActivate(@RequestBody java.util.Map<String, String> body) {
        try {
            String phone  = body.get("phone");
            String secret = body.get("secret");
            String code   = body.get("code");

            if (phone == null || secret == null || code == null)
                return ResponseEntity.badRequest().body(ApiResponse.error("Thiếu tham số"));

            if (!totpService.verifyCode(secret, code)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Mã xác thực không đúng"));
            }

            User user = userRepository.findByPhone(phone)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            user.setTotpSecret(secret);
            user.setTotpEnabled(true);
            userRepository.save(user);

            LoginResponse loginResponse = authService.loginByPhone(phone);
            return ResponseEntity.ok(ApiResponse.success("Kích hoạt thành công", loginResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Đăng nhập TOTP các lần sau: xác minh mã 6 số → JWT
     * Body: { "phone": "...", "code": "123456" }
     */
    @PostMapping("/totp/verify")
    public ResponseEntity<ApiResponse> totpVerify(@RequestBody java.util.Map<String, String> body) {
        try {
            String phone = body.get("phone");
            String code  = body.get("code");

            if (phone == null || code == null)
                return ResponseEntity.badRequest().body(ApiResponse.error("Thiếu tham số"));

            User user = userRepository.findByPhone(phone)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            if (!user.getTotpEnabled() || user.getTotpSecret() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("TOTP chưa được kích hoạt"));
            }

            if (!totpService.verifyCode(user.getTotpSecret(), code)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Mã xác thực không đúng hoặc đã hết hạn"));
            }

            LoginResponse loginResponse = authService.loginByPhone(phone);
            return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", loginResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

