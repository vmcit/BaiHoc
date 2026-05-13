# 📌 HƯỚNG DẪN REGISTER & PASSWORD RESET API

## 🎯 Tổng Quan

Hệ thống Authentication đã hoàn chỉnh với:
- ✅ API Đăng nhập (Login)
- ✅ API Đăng ký tài khoản (Register)
- ✅ API Quên mật khẩu (Forgot Password)
- ✅ API Reset mật khẩu (Reset Password)
- ✅ API Làm mới token (Refresh Token)
- ✅ API Đăng xuất (Logout)

---

## 📊 DATABASE SCHEMA

### Table: `tbl_users`
```
┌────┬──────────┬───────────────────┬──────────────┬──────────────┬──────────┬────────────────┬───────────┬────────────┬────────────┬────────────┐
│ id │ username │ email             │ password     │ full_name    │ phone    │ status         │ login_    │ last_login │ created_at │ updated_at │
│    │          │                   │              │              │          │                │ attempts  │            │            │            │
├────┼──────────┼───────────────────┼──────────────┼──────────────┼──────────┼────────────────┼───────────┼────────────┼────────────┼────────────┤
│ 1  │ admin    │ admin@example.com │ $2a$10$...   │ Admin User   │ 0901...  │ ACTIVE         │ 0         │ 1700000000 │ 1600000000 │ 1700000000 │
│ 2  │ alice    │ alice@example.com │ $2a$10$...   │ Alice Nguyen │ 0987...  │ ACTIVE         │ 0         │ 1700100000 │ 1600100000 │ 1700100000 │
│ 3  │ charlie  │ charlie@...       │ $2a$10$...   │ Charlie Tran │ null     │ LOCKED         │ 5         │ 1700200000 │ 1600200000 │ 1700200000 │
└────┴──────────┴───────────────────┴──────────────┴──────────────┴──────────┴────────────────┴───────────┴────────────┴────────────┴────────────┘
```
- `status`: `ACTIVE` | `INACTIVE` | `LOCKED` (tự động lock khi login sai 5 lần)
- `password`: BCrypt hash, không lưu plain text
- `login_attempts`: reset về 0 sau khi login thành công

---

### Table: `tbl_roles`
```
┌────┬──────────────┬────────────────────────────┬──────────┬────────────┐
│ id │ role_name    │ description                │ status   │ created_at │
├────┼──────────────┼────────────────────────────┼──────────┼────────────┤
│ 1  │ ROLE_ADMIN   │ Quản trị viên toàn quyền   │ ACTIVE   │ 1600000000 │
│ 2  │ ROLE_USER    │ Người dùng thường          │ ACTIVE   │ 1600000000 │
│ 3  │ ROLE_MOD     │ Kiểm duyệt viên            │ ACTIVE   │ 1600000000 │
└────┴──────────────┴────────────────────────────┴──────────┴────────────┘
```

---

### Table: `tbl_permissions`
```
┌────┬─────────────────┬──────────────────────────┬──────────┬────────────┐
│ id │ permission_name │ description              │ status   │ created_at │
├────┼─────────────────┼──────────────────────────┼──────────┼────────────┤
│ 1  │ CREATE_USER     │ Tạo tài khoản mới        │ ACTIVE   │ 1600000000 │
│ 2  │ EDIT_USER       │ Chỉnh sửa tài khoản      │ ACTIVE   │ 1600000000 │
│ 3  │ DELETE_USER     │ Xóa tài khoản            │ ACTIVE   │ 1600000000 │
│ 4  │ VIEW_REPORT     │ Xem báo cáo thống kê     │ ACTIVE   │ 1600000000 │
└────┴─────────────────┴──────────────────────────┴──────────┴────────────┘
```

---

### Table: `tbl_user_roles` *(join table Many-to-Many)*
```
┌─────────┬─────────┐
│ user_id │ role_id │
├─────────┼─────────┤
│ 1       │ 1       │  ← admin có ROLE_ADMIN
│ 2       │ 2       │  ← alice có ROLE_USER
│ 3       │ 2       │  ← charlie có ROLE_USER
└─────────┴─────────┘
```

---

### Table: `tbl_role_permissions` *(join table Many-to-Many)*
```
┌─────────┬───────────────┐
│ role_id │ permission_id │
├─────────┼───────────────┤
│ 1       │ 1             │  ← ROLE_ADMIN có CREATE_USER
│ 1       │ 2             │  ← ROLE_ADMIN có EDIT_USER
│ 1       │ 3             │  ← ROLE_ADMIN có DELETE_USER
│ 1       │ 4             │  ← ROLE_ADMIN có VIEW_REPORT
└─────────┴───────────────┘
```

---

### Table: `tbl_refresh_tokens`
```
┌────┬──────────────────────────────────┬─────────┬──────────────────────┬────────────┬────────────┬────────────┬────────────┐
│ id │ uid                              │ user_id │ refresh_token (TEXT) │ expires_at │ is_revoked │ created_at │ updated_at │
├────┼──────────────────────────────────┼─────────┼──────────────────────┼────────────┼────────────┼────────────┼────────────┤
│ 1  │ a1b2c3d4e5f6789012345678abcdef90 │ 1       │ eyJhbGciOiJIUzI1... │ 1700604800 │ false      │ 1700000000 │ 1700000000 │
│ 2  │ b2c3d4e5f6789012345678abcdef9012 │ 1       │ eyJhbGciOiJIUzI1... │ 1700604800 │ true       │ 1700100000 │ 1700200000 │
└────┴──────────────────────────────────┴─────────┴──────────────────────┴────────────┴────────────┴────────────┴────────────┘
```
- `uid`: 32-char hex (UUID không dấu gạch), dùng để lookup nhẹ thay vì dùng JWT string dài
- `is_revoked`: set `true` khi user logout
- `expires_at`: epoch ms — hết hạn sau 7 ngày
- Index: `uid`, `user_id`, `expires_at`

---

### Table: `tbl_password_reset_tokens`
```
┌────┬──────────────────────────────────┬─────────┬──────────────────────┬────────────┬──────────┬────────────┐
│ id │ uid                              │ user_id │ reset_token          │ expires_at │ is_used  │ created_at │
├────┼──────────────────────────────────┼─────────┼──────────────────────┼────────────┼──────────┼────────────┤
│ 1  │ c3d4e5f6789012345678abcdef901234 │ 2       │ abc123xyz789...      │ 1700900000 │ false    │ 1700800000 │
│ 2  │ d4e5f6789012345678abcdef90123456 │ 2       │ def456uvw012...      │ 1700950000 │ true     │ 1700850000 │
└────┴──────────────────────────────────┴─────────┴──────────────────────┴────────────┴──────────┴────────────┘
```
- `uid`: 32-char hex, dùng để lookup nhanh
- `is_used`: set `true` sau khi user reset mật khẩu thành công (dùng 1 lần duy nhất)
- `expires_at`: hết hạn sau 15 phút
- Index: `uid`, `user_id`, `expires_at`

---

### Quan hệ giữa các bảng
```
tbl_users (1) ──────────────────── (N) tbl_refresh_tokens
tbl_users (1) ──────────────────── (N) tbl_password_reset_tokens
tbl_users (N) ── tbl_user_roles ── (N) tbl_roles
tbl_roles (N) ── tbl_role_permissions ── (N) tbl_permissions
```

---

## 🔧 CẤU HÌNH EMAIL (GMAIL)

Để gửi email thành công, cập nhật file `application.properties`:

```properties
# Gmail SMTP Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

### Hướng dẫn tạo Gmail App Password:

1. **Bước 1:** Vào https://myaccount.google.com
2. **Bước 2:** Click vào "Security" (Bảo mật)
3. **Bước 3:** Enable "2-Step Verification" (Xác thực 2 bước) nếu chưa bật
4. **Bước 4:** Quay lại Security, tìm "App passwords"
5. **Bước 5:** Chọn "Mail" và "Windows Computer"
6. **Bước 6:** Google sẽ tạo password 16 ký tự
7. **Bước 7:** Copy password đó vào `spring.mail.password`

---

## 📡 API ENDPOINTS

### 1️⃣ API ĐĂNG KÝ (Register)

**URL:** `POST /api/users/register`

**Request Body:**
```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "password123",
  "confirmPassword": "password123",
  "fullName": "New User",
  "phone": "0987654321"
}
```

**Thành công (201 Created):**
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "userId": 5,
    "username": "newuser",
    "email": "newuser@example.com",
    "fullName": "New User",
    "message": "Register successful. You can now login."
  },
  "timestamp": 1683624000000
}
```

**Lỗi (400 Bad Request):**
```json
{
  "code": 400,
  "message": "Register failed: Username already exists",
  "timestamp": 1683624000000
}
```

---

### 2️⃣ API QUÊN MẬT KHẨU (Forgot Password)

**URL:** `POST /api/users/forgot-password`

**Request Body:**
```json
{
  "email": "user@example.com"
}
```

**Thành công (200 OK):**
```json
{
  "code": 200,
  "message": "Password reset email has been sent. Please check your email.",
  "timestamp": 1683624000000
}
```

**Email được gửi sẽ chứa:**
```
Xin chào user,

Bạn đã yêu cầu reset mật khẩu. Vui lòng sử dụng mã reset bên dưới:

🔐 MÃ RESET: abc123def456...

Hoặc click vào link:
http://localhost:8080/reset-password?token=abc123def456...

⚠️  Mã này sẽ hết hạn sau 15 phút.
⚠️  Nếu bạn không yêu cầu reset mật khẩu, vui lòng bỏ qua email này.
```

---

### 3️⃣ API RESET MẬT KHẨU (Reset Password)

**URL:** `POST /api/users/reset-password`

**Request Body:**
```json
{
  "resetToken": "abc123def456...",
  "newPassword": "newpassword123",
  "confirmPassword": "newpassword123"
}
```

**Thành công (200 OK):**
```json
{
  "code": 200,
  "message": "Password reset successful. You can now login with your new password.",
  "timestamp": 1683624000000
}
```

**Lỗi (400 Bad Request):**
```json
{
  "code": 400,
  "message": "Password reset failed: Reset token expired or already used",
  "timestamp": 1683624000000
}
```

---

### 4️⃣ API ĐĂNG NHẬP (Login)

**URL:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Thành công (200 OK):**
```json
{
  "code": 200,
  "message": "Login successful",
  "data": {
    "userId": 1,
    "username": "admin",
    "email": "admin@example.com",
    "fullName": "Administrator",
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 86400000,
    "roles": ["ADMIN"],
    "permissions": ["CREATE_USER", "EDIT_USER", "DELETE_USER", "VIEW_REPORT"]
  },
  "timestamp": 1683624000000
}
```

---

## 🔐 CÁC TRẠNG THÁI USER

| Status | Mô tả |
|--------|-------|
| `ACTIVE` | Tài khoản hoạt động bình thường |
| `INACTIVE` | Tài khoản bị vô hiệu hóa |
| `LOCKED` | Tài khoản bị khóa vì quá nhiều lần login sai (5 lần) |

---

## 📋 VALIDATION RULES

### Register:
- ✅ Username: Bắt buộc, phải duy nhất, 3-50 ký tự
- ✅ Email: Bắt buộc, phải duy nhất, định dạng email hợp lệ
- ✅ Password: Bắt buộc, tối thiểu 6 ký tự
- ✅ Confirm Password: Phải trùng với Password
- ✅ Full Name: Tùy chọn
- ✅ Phone: Tùy chọn

### Forgot Password:
- ✅ Email: Bắt buộc, phải tồn tại trong database

### Reset Password:
- ✅ Reset Token: Bắt buộc, phải hợp lệ và chưa hết hạn (15 phút)
- ✅ New Password: Tối thiểu 6 ký tự
- ✅ Confirm Password: Phải trùng với New Password

---

## 🧪 TEST CREDENTIALS

```
ADMIN:     admin / admin123
USER:      user1 / user1234
USER:      user2 / user2234
MODERATOR: moderator / mod12345
```

---

## 🚀 FLOW QUÊN MẬT KHẨU

```
1. User click "Quên mật khẩu" trên frontend
   ↓
2. User nhập email → gọi POST /api/users/forgot-password
   ↓
3. Backend generate reset token (hết hạn 15 phút)
   ↓
4. Backend gửi email chứa reset token
   ↓
5. User check email, copy reset token
   ↓
6. User nhập mật khẩu mới + reset token
   ↓
7. Gọi POST /api/users/reset-password
   ↓
8. Backend verify token, update password
   ↓
9. Email xác nhận reset thành công
   ↓
10. User login với mật khẩu mới
```

---

## 📝 FEATURES NỘI BỘ

### Security:
- ✅ Password hashed với BCrypt
- ✅ JWT Token cho authentication
- ✅ Refresh Token (7 ngày)
- ✅ Access Token (1 ngày)
- ✅ Reset Token hết hạn sau 15 phút
- ✅ One-time use reset token

### Email:
- ✅ Welcome email sau khi register
- ✅ Reset password email
- ✅ Confirmation email sau reset thành công

### Account Protection:
- ✅ Login attempts tracking
- ✅ Auto lock sau 5 lần login sai
- ✅ Token revocation

---

## 🐛 TROUBLESHOOTING

### Lỗi: "Could not send email"
- ✅ Check app password có đúng không
- ✅ Check email address có đúng không
- ✅ Check Internet connection
- ✅ Check Gmail account có 2FA không

### Lỗi: "Reset token expired"
- ✅ Reset token hết hạn sau 15 phút
- ✅ Request forgot password lại để lấy token mới

### Lỗi: "Username/Email already exists"
- ✅ Username hoặc email đã tồn tại
- ✅ Sử dụng username/email khác

---

## 📚 ENTITIES

1. **User** - Người dùng
2. **Role** - Vai trò (ADMIN, USER, MODERATOR)
3. **Permission** - Quyền hạn
4. **RefreshToken** - Token để refresh access token
5. **PasswordResetToken** - Token để reset password

---

## ✨ ĐIỂM NỎI BẬT

✅ RESTful API design
✅ Request/Response DTOs
✅ Global error handling
✅ JWT Authentication
✅ Email integration (Gmail SMTP)
✅ Token expiration & refresh
✅ Role-based access control
✅ Transaction management
✅ Input validation
✅ Security best practices

---

**Happy coding! 🚀**

