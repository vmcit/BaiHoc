# API Guide - BaiHoc Spring Boot Project

Base URL: `http://localhost:8080`

---

## Khởi động dự án

```powershell
$env:JAVA_HOME='C:\Users\cuongmanh.vu\.jdks\ms-17.0.18'
cd "D:\Book\ocp\baihoc\BaiHoc"
mvn spring-boot:run
```

Cấu hình tại `src/main/resources/application.properties`:
- **DB**: MariaDB `localhost:3306/my_db` | user: `root` | pass: `12345`
- **Access token** hết hạn: 24 giờ
- **Refresh token** hết hạn: 7 ngày
- **Roles**: `ROLE_ADMIN` — toàn quyền | `ROLE_USER` — chỉ xem sản phẩm

---

## Danh sách API đầy đủ

### 🔓 Auth — Không cần token

| Method | URL | Mô tả |
|--------|-----|-------|
| POST | `/api/users/register` | Đăng ký tài khoản |
| POST | `/api/auth/login` | Đăng nhập, nhận access + refresh token |
| POST | `/api/auth/refresh-token` | Làm mới access token bằng refresh token |
| POST | `/api/users/forgot-password` | Gửi email link reset mật khẩu |
| POST | `/api/users/reset-password` | Đặt lại mật khẩu bằng token từ email |

### 🔐 Auth — Cần token (mọi role)

| Method | URL | Mô tả |
|--------|-----|-------|
| POST | `/api/auth/logout` | Đăng xuất, revoke tất cả refresh token |
| GET | `/api/auth/validate-token` | Kiểm tra token còn hạn không |

### 🛒 Public — Không cần token

| Method | URL | Mô tả |
|--------|-----|-------|
| GET | `/api/products` | Danh sách sản phẩm (public) |

### 👤 Protected — Cần token `ROLE_USER` hoặc `ROLE_ADMIN`

| Method | URL | Mô tả |
|--------|-----|-------|
| GET | `/api/protected/me` | Xem thông tin tài khoản của mình |
| GET | `/api/protected/products` | Danh sách sản phẩm (có bảo vệ) |

### 👑 Admin — Chỉ `ROLE_ADMIN`

| Method | URL | Mô tả |
|--------|-----|-------|
| GET | `/api/protected/admin/users` | Danh sách tất cả user |
| GET | `/api/protected/admin/stats` | Thống kê hệ thống |
| DELETE | `/api/protected/admin/users/{id}` | Xóa user theo id |

---

## Test từng bước với CURL (cmd)

> Thay `TOKEN` bằng accessToken thực tế nhận được sau khi login.

---

### 1 — Đăng ký tài khoản

```cmd
curl -X POST http://localhost:8080/api/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"alice\",\"email\":\"alice@gmail.com\",\"password\":\"123456\",\"confirmPassword\":\"123456\",\"fullName\":\"Alice Nguyen\",\"phone\":\"0987654321\"}"
```

**201 Created:**
```json
{ "success": true, "message": "Register successful", "data": { "id": 2, "username": "alice" } }
```

---

### 2 — Đăng nhập (lấy token)

```cmd
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"alice\",\"password\":\"123456\"}"
```

**200 OK:**
```json
{
  "success": true,
  "data": {
    "accessToken":  "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "roles": ["ROLE_USER"]
  }
}
```

> **Copy `accessToken`** — dùng thay `TOKEN` ở các bước dưới.

---

### 3 — Xem thông tin tài khoản mình

```cmd
curl http://localhost:8080/api/protected/me ^
  -H "Authorization: Bearer TOKEN"
```

**200 OK:**
```json
{ "data": { "userId": 2, "username": "alice", "roles": ["ROLE_USER"] } }
```

---

### 4 — Xem sản phẩm (ROLE_USER được phép)

```cmd
curl http://localhost:8080/api/protected/products ^
  -H "Authorization: Bearer TOKEN"
```

---

### 5 — Xem danh sách user — ROLE_USER → 403

```cmd
curl http://localhost:8080/api/protected/admin/users ^
  -H "Authorization: Bearer USER_TOKEN"
```

**403 Forbidden:**
```json
{ "success": false, "message": "Bạn không có quyền. Yêu cầu: ROLE_ADMIN" }
```

---

### 6 — Xem danh sách user — ROLE_ADMIN → 200

```cmd
curl http://localhost:8080/api/protected/admin/users ^
  -H "Authorization: Bearer ADMIN_TOKEN"
```

**200 OK:**
```json
{
  "data": [
    { "id": 1, "username": "admin",   "roles": ["ROLE_ADMIN"], "status": "ACTIVE" },
    { "id": 2, "username": "alice",   "roles": ["ROLE_USER"],  "status": "ACTIVE" },
    { "id": 3, "username": "bob",     "roles": ["ROLE_USER"],  "status": "ACTIVE" },
    { "id": 4, "username": "charlie", "roles": ["ROLE_USER"],  "status": "LOCKED" }
  ]
}
```

---

### 7 — Thống kê hệ thống (admin only)

```cmd
curl http://localhost:8080/api/protected/admin/stats ^
  -H "Authorization: Bearer ADMIN_TOKEN"
```

---

### 8 — Xóa user (admin only)

```cmd
curl -X DELETE http://localhost:8080/api/protected/admin/users/3 ^
  -H "Authorization: Bearer ADMIN_TOKEN"
```

---

### 9 — Làm mới access token

```cmd
curl -X POST http://localhost:8080/api/auth/refresh-token ^
  -H "Content-Type: application/json" ^
  -d "{\"refreshToken\":\"REFRESH_TOKEN\"}"
```

**200 OK:** trả về `accessToken` mới, `refreshToken` cũ vẫn dùng được.

---

### 10 — Kiểm tra token còn hạn không

```cmd
curl http://localhost:8080/api/auth/validate-token ^
  -H "Authorization: Bearer TOKEN"
```

---

### 11 — Đăng xuất

```cmd
curl -X POST http://localhost:8080/api/auth/logout ^
  -H "Authorization: Bearer TOKEN"
```

Tất cả refresh token của user bị **revoke** ngay lập tức.

---

### 12 — Quên mật khẩu

**12a. Gửi email reset** (cần cấu hình SMTP):

```cmd
curl -X POST http://localhost:8080/api/users/forgot-password ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"alice@gmail.com\"}"
```

**12b. Đặt lại mật khẩu** bằng token nhận từ email:

```cmd
curl -X POST http://localhost:8080/api/users/reset-password ^
  -H "Content-Type: application/json" ^
  -d "{\"token\":\"TOKEN_TU_EMAIL\",\"newPassword\":\"newpass123\",\"confirmPassword\":\"newpass123\"}"
```

---

### 13 — Sản phẩm public (không cần token)

```cmd
curl http://localhost:8080/api/products
```

---

## Lỗi thường gặp

| HTTP | Message | Nguyên nhân | Cách xử lý |
|------|---------|-------------|-----------|
| — | `Connection refused` | App chưa chạy | Chạy `mvn spring-boot:run` |
| 400 | `Username is required` | Body thiếu field | Kiểm tra JSON gửi lên |
| 401 | `Token không hợp lệ` | Thiếu / sai header | Thêm `Authorization: Bearer <token>` |
| 401 | `Invalid or expired token` | Token hết hạn (24h) | Gọi `/api/auth/refresh-token` |
| 403 | `Bạn không có quyền` | Sai role | Dùng tài khoản có đúng role |
| 404 | `User not found` | Username chưa tồn tại | Đăng ký trước |
| 400 | `Invalid password` | Sai mật khẩu | Thử lại (sai 5 lần → bị khóa) |
| 500 | `Could not connect to DB` | MariaDB chưa chạy | Khởi động MariaDB service |

---

## Danh sách API

| # | Method | URL | Mô tả | Cần token? |
|---|--------|-----|-------|-----------|
| 1 | POST | `/api/users/register` | Đăng ký tài khoản | Không |
| 2 | POST | `/api/auth/login` | Đăng nhập | Không |
| 3 | POST | `/api/auth/refresh-token` | Làm mới access token | Không |
| 4 | POST | `/api/auth/logout` | Đăng xuất | Có |
| 5 | GET | `/api/auth/validate-token` | Kiểm tra token hợp lệ | Có |
| 6 | GET | `/api/protected/user-info` | Lấy thông tin user | Có |
| 7 | GET | `/api/protected/admin-only` | API chỉ admin | Có |
| 8 | POST | `/api/users/forgot-password` | Quên mật khẩu (gửi email) | Không |
| 9 | POST | `/api/users/reset-password` | Đặt lại mật khẩu | Không |
| 10 | GET | `/api/products` | Danh sách sản phẩm | Không |

---

## Hướng dẫn test từng bước

### BƯỚC 1 — Đăng ký tài khoản

```cmd
curl -X POST http://localhost:8080/api/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"email\":\"testuser@gmail.com\",\"password\":\"123456\",\"confirmPassword\":\"123456\",\"fullName\":\"Test User\",\"phone\":\"0987654321\"}"
```

**Response thành công (201):**
```json
{
  "success": true,
  "message": "Register successful",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "testuser@gmail.com",
    "fullName": "Test User"
  }
}
```

---

### BƯỚC 2 — Đăng nhập → lấy token

```cmd
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"password\":\"123456\"}"
```

**Response thành công (200):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "id": 1,
    "username": "testuser",
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresAt": 1234567890000,
    "roles": ["ROLE_USER"],
    "permissions": ["READ"]
  }
}
```

> **Copy giá trị `accessToken`** để dùng ở các bước tiếp theo.

---

### BƯỚC 3 — Gọi API cần token

Thay `YOUR_ACCESS_TOKEN` bằng token lấy từ bước 2:

```cmd
curl -X GET http://localhost:8080/api/protected/user-info ^
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

**Response thành công (200):**
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "username": "testuser",
    "message": "This is protected resource"
  }
}
```

**Response khi không có token (401):**
```json
{
  "success": false,
  "message": "Authorization header is missing or invalid"
}
```

---

### BƯỚC 4 — Làm mới token (khi access token hết hạn)

```cmd
curl -X POST http://localhost:8080/api/auth/refresh-token ^
  -H "Content-Type: application/json" ^
  -d "{\"refreshToken\":\"YOUR_REFRESH_TOKEN\"}"
```

**Response:** trả về `accessToken` mới, `refreshToken` cũ vẫn giữ nguyên.

---

### BƯỚC 5 — Kiểm tra token còn hạn không

```cmd
curl -X GET http://localhost:8080/api/auth/validate-token ^
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

### BƯỚC 6 — Đăng xuất

```cmd
curl -X POST http://localhost:8080/api/auth/logout ^
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

Tất cả refresh token của user sẽ bị revoke. Token cũ không dùng được nữa.

---

### BƯỚC 7 — Quên mật khẩu

**7a. Gửi email reset** (cần cấu hình SMTP trong `application.properties`):

```cmd
curl -X POST http://localhost:8080/api/users/forgot-password ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"testuser@gmail.com\"}"
```

**Response:**
```json
{
  "success": true,
  "message": "Password reset email has been sent. Please check your email."
}
```

**7b. Đặt lại mật khẩu** (dùng token nhận từ email):

```cmd
curl -X POST http://localhost:8080/api/users/reset-password ^
  -H "Content-Type: application/json" ^
  -d "{\"token\":\"TOKEN_TU_EMAIL\",\"newPassword\":\"newpass123\",\"confirmPassword\":\"newpass123\"}"
```

---

### BƯỚC 8 — Xem danh sách sản phẩm (public, không cần token)

```cmd
curl http://localhost:8080/api/products
```

---

## Lỗi thường gặp

| Lỗi | Nguyên nhân | Cách sửa |
|-----|-------------|----------|
| `Connection refused` | App chưa chạy | Chạy `mvn spring-boot:run` |
| `401 Unauthorized` | Token sai hoặc thiếu header | Thêm `Authorization: Bearer <token>` |
| `Invalid or expired token` | Token hết hạn (24h) | Gọi `/api/auth/refresh-token` |
| `User not found` | Username không tồn tại | Đăng ký trước ở `/api/users/register` |
| `Invalid password` | Sai mật khẩu (thử sai 5 lần → bị khóa) | Đặt lại mật khẩu hoặc unlock DB |
| `Could not connect to DB` | MariaDB chưa chạy | Khởi động MariaDB service |
